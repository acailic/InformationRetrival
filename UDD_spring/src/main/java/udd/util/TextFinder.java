package udd.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.PositionWrapper;
import org.apache.pdfbox.util.TextPosition;

public class TextFinder extends PDFTextStripper {

    public TextFinder() throws IOException {
        super();
        cnt = 0;
        imageMap = new HashMap<String, HashMap<String, Float>>();
    }

    private static Map<String, HashMap<String, Float>> imageMap = new HashMap<String, HashMap<String, Float>>();
    private static int cnt = 0;
    
    @Override
    protected void startPage(PDPage page) throws IOException {
        super.startPage(page);
    }

    @Override
    public void processOperator(PDFOperator operator, List<COSBase> arguments)
            throws IOException {

        if ("cm".equals(operator.getOperation())) {
        	//this.handleLineSeparation(current, lastPosition, lastLineStartPosition, maxHeightForLine)
        	if(((COSNumber)arguments.get(3)).floatValue() == 1)
        		return;
            float width = ((COSNumber)arguments.get(0)).floatValue();
            float height = ((COSNumber)arguments.get(3)).floatValue();
            float x = ((COSNumber)arguments.get(4)).floatValue();
            float y = ((COSNumber)arguments.get(5)).floatValue();
            HashMap<String, Float> image = new HashMap<String, Float>();
            image.put("height", ((COSNumber)arguments.get(3)).floatValue());
            image.put("y", ((COSNumber)arguments.get(5)).floatValue());
            imageMap.put("img_" + cnt, image);
            cnt++;
            // process image coordinates
            System.out.println("Image found. Coordinates are: width - " + width + ", height - " + height + ", X - " + x + ", Y - " + y);
        }
        else 
        super.processOperator(operator, arguments);
    }
    
    public static void main(String[] args) throws Exception
    {
        try{
        	PositionWrapper pw;
            PDDocument document = PDDocument.load(
                "C:\\Users\\Aleksandar\\Desktop\\files\\domaci.pdf" );
            		
            TextFinder printer = new TextFinder();
            List allPages = document.getDocumentCatalog().getAllPages();
            for( int i=0; i<allPages.size(); i++ )
            {
                PDPage page = (PDPage)allPages.get( i );
                int pageNum = i+1;
                System.out.println( "Processing page: " + pageNum );
                printer.processStream( page, page.findResources(),
                    page.getContents().getStream() );
            }
            
            for(int i = 0; i < imageMap.size(); ++i){
            	System.out.println("img" + i + ": " + "height is " + imageMap.get("img" + i).get("height") + ", y is" + imageMap.get("img" + i).get("y"));
            }
        }
        finally
        {
        }
    }
    
   
    
    public Map<String, HashMap<String, Float>> getImageMap(){
    	return imageMap;
    }
}