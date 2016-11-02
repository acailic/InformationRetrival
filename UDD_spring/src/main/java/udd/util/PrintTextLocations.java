package udd.util;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

public class PrintTextLocations extends PDFTextStripper {

    public PrintTextLocations() throws IOException {
        super.setSortByPosition(true);
    }
    
    private static List<TextPosition> charactersInDocument = new ArrayList<TextPosition>();
    private static ArrayList<TextPosition> charactersOfPage = new ArrayList<TextPosition>();
    private static Map<Integer, ArrayList<TextPosition>> charactersOnPage = new HashMap<Integer, ArrayList<TextPosition>>();
    
    public static void main(String[] args) throws Exception {

        PDDocument document = null;
        try {
            File input = new File("C:\\Users\\Aleksandar\\Desktop\\files\\naslov.pdf");
            document = PDDocument.load(input);
            
            PrintTextLocations printer = new PrintTextLocations();
            List allPages = document.getDocumentCatalog().getAllPages();
            int totalImages = 0;
            TextFinder tf = new TextFinder();
            Map<Integer, ArrayList<String>> imgsOnPage = new HashMap<Integer, ArrayList<String>>(); //broj stranice i sve slike na njoj
            Map<String, HashMap<String, Float>> imageMap = new HashMap<String, HashMap<String, Float>>(); //sve slike u tekstu i njihova visina
            for (int i = 0; i < allPages.size(); i++) {
            	ArrayList<String> pageImgs = new ArrayList<String>();
                PDPage page = (PDPage) allPages.get(i);
                PDResources pdResources = ((PDPage) allPages.get(i)).getResources();
                Map pageImages = pdResources.getImages();
                if (pageImages != null) {
                    Iterator imageIter = pageImages.keySet().iterator();
                    while (imageIter.hasNext()) {
                        String key = (String) imageIter.next();
                        PDXObjectImage pdxObjectImage = (PDXObjectImage) pageImages.get(key);
                        pdxObjectImage.write2file("C:\\Users\\Aleksandar\\Desktop\\files\\img" + "_" + totalImages);
                        pageImgs.add("img_" + totalImages);
                        totalImages++;
                    }
                }
                if(!pageImgs.isEmpty()){
                	imgsOnPage.put(i+1, pageImgs);
                }
                System.out.println("Processing page: " + i);
                PDStream contents = page.getContents();
                if (contents != null) {
                    printer.processStream(page, page.findResources(), page.getContents().getStream());
                    tf.processStream(page, page.findResources(), page.getContents().getStream());
                    imageMap.putAll(tf.getImageMap());
                }
                
                charactersOnPage.put(i + 1, charactersOfPage);
                charactersOfPage = new ArrayList<TextPosition>();
            }
            Map<Integer, ArrayList<TextPosition>> lines = new HashMap<Integer, ArrayList<TextPosition>>(); //na nivou celog teksta linije
            int lineCnt = 1;
            /*
    			1. I extract all the characters of the document (called glyphs) and store them in a list.
    			2. I do an analysis of the coordinates of each glyph, looping over the list. If they overlap (if the top of the current glyph is contained between the top and bottom of the preceding/or the bottom of the current glyph is contained between the top and bottom of the preceding one), I add it to the same line.
    			3. At this point, I have extracted the different lines of the document (be careful, if your document is multi-column, the expression "lines" means all the glyphs that overlap vertically, ie the text of all the columns that have the same vertical coordinates).
    			4. Then, you can compare the left coordinate of the current glyph to the right coordinate of the preceding one to determine if they belong to the same word or not (the PDFTextStripper class provides a getSpacingTolerance() method that gives you, based on trials and errors, the value of a "normal" space. If the difference between the right and the left coordinates is lower than this value, both glyphs belong to the same word.
             	*/
            for(int i = 0; i < charactersInDocument.size(); i++){
            	if(i == 0){
            		ArrayList<TextPosition> line = new ArrayList<TextPosition>();
            		line.add(charactersInDocument.get(i));
            		lines.put(1, line);
            	} else {
            		TextPosition previousChar = charactersInDocument.get(i - 1);
            		TextPosition currentChar = charactersInDocument.get(i); 
            		if(currentChar.getHeightDir() <= previousChar.getHeightDir() && 
            				-0.3 <=(currentChar.getY() - previousChar.getY()) &&  
            				(currentChar.getY() - previousChar.getY()) <= 0.3)
            				lines.get(lineCnt).add(currentChar);
            		else{
            			ArrayList<TextPosition> line = new ArrayList<TextPosition>();
            			line.add(currentChar);
            			lineCnt++;
            			lines.put(lineCnt, line);
            		}
            	}
            }
            
            Map<Integer, ArrayList<TextPosition>> linesOnPage = new HashMap<Integer, ArrayList<TextPosition>>();
            
            for(int i = 1; i < lines.size(); ++i){
            	if(lines.get(i).equals(charactersOnPage.get(i)))
            		linesOnPage.put(i, lines.get(i));
            }
            for(int i = 1; i < linesOnPage.size(); ++i){
            	System.out.println("Page " + i);
            	System.out.println("Line number " + i + ": ");
            	String lineString = "";
            	for(TextPosition tp : linesOnPage.get(i)){
            		lineString += tp.getCharacter();
            	}
            	System.out.println(lineString);
            }
            Map<Integer, TextPosition> representationChars = new HashMap<Integer, TextPosition>(); //jedan karakter po liniji da reprezentuje Y koordinatu cele linije
            for(int i = 1; i < lines.size(); i++){
            	System.out.println("Line number " + i + ": ");
            	String lineString = "";
            	representationChars.put(i, lines.get(i).get(0));
            	for(TextPosition tp : lines.get(i)){
            		lineString += tp.getCharacter();
            	}
            	System.out.println(lineString);
            }
            
            Map<Integer, Integer> lineDifferences = new HashMap<Integer, Integer>(); //broj linije pre koje dolazi slika je kljuc, vrednost je razlika izmedju te i prethodne linije
            Map<String, Integer> lineNumForImages = new HashMap<String, Integer>(); //broj linije posle koje dolazi slika i naziv slike
            for(int i = 1; i < representationChars.size(); ++i){
            	if(i == 1)
            		continue;
            	TextPosition prev = representationChars.get(i - 1);
            	TextPosition curr = representationChars.get(i);
            	float diff = curr.getY() - prev.getY();
            	System.out.println("Difference " + i + ": " + diff);
            	int diffInt = (int)diff;
            	lineDifferences.put(i, diffInt);
            }
            
			if (!imgsOnPage.isEmpty()) {
				int pageNums = 1;
				for (int i = 2; i < lineDifferences.size(); i++) {
					if (lineDifferences.get(i) < 0){
						pageNums++;
						continue;
					}
					if (imgsOnPage.containsKey(pageNums)){
						ArrayList<String> imgs = imgsOnPage.get(pageNums);
						for(String image : imgs){
							if (lineDifferences.get(i)
									- imageMap.get(image).get("height") < 0)
								continue;
							else if (lineDifferences.get(i)
									- imageMap.get(image).get("height") <= 70
									/*&& !lineNumForImages.containsValue(i - 1)*/) {
								lineNumForImages.put(image, i - 1);
							}
						}
					}
				}

				for (int i = 0; i < lineNumForImages.size(); ++i) {
					System.out.println("Slika " + "img" + i
							+ " dolazi nakon linije broj "
							+ lineNumForImages.get("img_" + i));
				}
			}
            /*for(int i = 0; i < imageMap.size(); ++i){
            	System.out.println("img" + i + ": " + "height is " + imageMap.get("img" + i).get("height") + ", y is" + imageMap.get("img" + i).get("y"));
            }*/
            
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    /**
     * @param text The text to be processed
     */
    @Override /* this is questionable, not sure if needed... */
    protected void processTextPosition(TextPosition text) {
    	charactersInDocument.add(text);
    	charactersOfPage.add(text);
        /*System.out.println("String[" + text.getXDirAdj() + ","
                + text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale="
                + text.getXScale() + " height=" + text.getHeightDir() + " space="
                + text.getWidthOfSpace() + " width="
                + text.getWidthDirAdj() + "]" + text.getCharacter());*/
    }
}
