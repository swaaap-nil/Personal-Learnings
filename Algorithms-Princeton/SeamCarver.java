import java.awt.Color;
import java.util.Arrays;
import edu.princeton.cs.algs4.AcyclicSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;
public class SeamCarver {
    
     
     
     private EdgeWeightedDigraph Graph;
     private Picture picture;
     private double[][] energyMap;
     

     private void throwIllegalException(String s){
          throw new IllegalArgumentException(s);
     }
   // create a seam carver object based on the given picture
      public SeamCarver(Picture picture){
          
          if(picture == null) throwIllegalException("Null picture in constructor");

          this.picture = new Picture(picture);
          // this.width = this.picture.width();
          // this.height = this.picture.height();
          
          // System.out.println("width: "+picture.width() + " height: "+picture.height());
          
          buildEnergyMap();

        }
       
     private void buildEnergyMap() {
          this.energyMap = new double[height()][width()];
          for(int col=0;col<energyMap[0].length;col++){
               for(int row =0 ; row< energyMap.length ; row++){
                    energyMap[row][col] = calcEnergyAtPixel(col, row);
               }
          }

          // System.out.println(arraysAreEqual(newEnergyMap, energyMap));

     }

     private double[][] transposeMatrix(double[][] matrix) {
          int numRows = matrix.length;
          int numCols = matrix[0].length;
          double[][] transposedMatrix = new double[numCols][numRows];
      
          for (int col = 0; col < numCols; col++) {
              for (int row = 0; row < numRows; row++) {
                  transposedMatrix[col][row] = matrix[row][col];
              }
          }
      
          return transposedMatrix;
      }

     public int[] findHorizontalSeam(){

          Graph = new EdgeWeightedDigraph(numVertices()+4);

          for(int x = 0 ; x < this.height() ; x++){
               for(int y=0; y< this.width() ; y++){
                    int v = pointToVertexConvertor(x,y);
                      // System.out.println("Processing Vertex: "+v);

                    if(y==0){
                         pointHelperNode(Graph, numVertices(), x, y);
                    }

                    /* Uses Energy Map */
                    if(oneMoreColExists(y))
                    pointToRight(v,x,y);  //point this vertex to all elements of right column
               }
          }
          
          int[] path = new int[width()]; int i =-1;
          AcyclicSP sp = new AcyclicSP(Graph,numVertices());
          for(DirectedEdge e :  sp.pathTo(numVertices()+2)){
               int to = e.to();
               if(to>=numVertices()) continue;
               int[] point = vertexToPointConvertor(to);
               path[++i] = point[0];
          }
          //System.out.println(Arrays.toString(path));
          return path;
     }

     public int[] findVerticalSeam(){

          this.energyMap=transposeMatrix(energyMap);
          this.picture = transpose(picture);
          int[] path = findHorizontalSeam();
          this.energyMap = transposeMatrix(energyMap);
          this.picture=transpose(picture);
          return path;
          
   } 

     private int numVertices() {
     return (height() * width());
     // System.out.println("numVertices: "+this.numVertices);
}
     
     private void printCurrEnergyMap() {
           System.out.println("\nEnergyMap\n");
          
           for (int row = 0; row < height(); row++) {
               for (int col = 0; col < width(); col++) {
               System.out.print(energyMap[row][col]+" ");
          }
          System.out.println();
      }
     }
     
     private void pointHelperNode(EdgeWeightedDigraph G,int v, int x, int y) {
          int w = pointToVertexConvertor(x, y);
          double weight = 0;
          DirectedEdge e = new DirectedEdge(v, w, weight);
           //System.out.println("pointing "+v+"->"+w+" with weight: "+weight);
          G.addEdge(e);

     }

     private boolean oneMoreColExists(int col) {
          return col <= this.width() -1;
     }

     private void pointToRight(int v,int row,int col) {

          int w; double weight;
          
               if(col != width()-1) //normal case
               {      
                    int lowerLimit = (row-1<=0)?0 : row-1;
                    int upperLimit = (row+1>=height())? height()-1 : row+1; 
                    

                    for(int rowNumber = lowerLimit ; rowNumber <= upperLimit ; rowNumber++){
                     w = pointToVertexConvertor(rowNumber, col+1);
                    // weight = energy(col+1, rowNumber);
                         
                         //System.err.println(rowNumber+" "+(col));
                         weight = energyMap[rowNumber][col+1];
                         DirectedEdge e = new DirectedEdge(v,w, weight);
                         // System.out.println("pointing "+v+"->"+w+" with weight: "+weight);
                         Graph.addEdge(e);
                    }
               }
               else{ // when in last col
                    w= height()*width()+2;
                    weight = 0.0;
                    DirectedEdge e = new DirectedEdge(v,w, weight);
                      //System.out.println("pointing "+v+"->"+w+" with weight: "+weight);
                    Graph.addEdge(e);
               }
          }   

     private int pointToVertexConvertor(int x, int y) { 
          return x * this.width() + y;
     }

     private int[] vertexToPointConvertor(int vertex){
     int rowIndex = vertex / width();
     int columnIndex = vertex % width(); 
     return new int[]{rowIndex,columnIndex};
 }
     
     public Picture picture(){
      return new Picture(this.picture);
   }

     public int width(){
     return this.picture.width();
   }

     public int height(){
     return this.picture.height();
   } 
     
     public double energy(int x,int y){
          validateY(y);
          validateX(x);
          return this.energyMap[y][x];
     }
     
     private double calcEnergyAtPixel(int x, int y) {
          validateY(y);
          validateX(x);

     // Check if the pixel is at the border of the picture
     if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
         return 1000.0; // Border pixel energy
     }

     // Get the colors of the current pixel and its neighboring pixels
     Color leftColor = this.picture.get(x-1,y);
     Color rightColor = this.picture.get(x+1,y);
     Color topColor = this.picture.get(x,y-1);
     Color bottomColor = this.picture.get(x,y + 1);

     // Calculate the x-gradient and y-gradient components for each color channel
     int deltaXRed = rightColor.getRed() - leftColor.getRed();
     int deltaXGreen = rightColor.getGreen() - leftColor.getGreen();
     int deltaXBlue = rightColor.getBlue() - leftColor.getBlue();

     int deltaYRed = bottomColor.getRed() - topColor.getRed();
     int deltaYGreen = bottomColor.getGreen() - topColor.getGreen();
     int deltaYBlue = bottomColor.getBlue() - topColor.getBlue();

     // Calculate the square of the x-gradient and y-gradient for each color channel
     int deltaX2 = deltaXRed * deltaXRed + deltaXGreen * deltaXGreen + deltaXBlue * deltaXBlue;
     int deltaY2 = deltaYRed * deltaYRed + deltaYGreen * deltaYGreen + deltaYBlue * deltaYBlue;

     // Calculate the dual-gradient energy for the pixel
     double energy = Math.sqrt(deltaX2 + deltaY2);

     return energy;
 }

     private void validateX(int x) {
          if (x < 0 || x >= width()) {
               throw new IllegalArgumentException("X out of Range [0," + (width()-1) + "]: "+x);
           }
     }

     private void validateY(int y) {
          if (y < 0 || y >= height()) {
               throw new IllegalArgumentException("Y out of Range [0," + (height()-1) + "]: "+y);
           }
     }
     
     private Picture transpose(Picture picture) {
     Picture transposed = new Picture(picture.height(), picture.width());
     for (int y = 0; y < picture.height(); y++) {
         for (int x = 0; x < picture.width(); x++) {
             int c = picture.getRGB(x, y);
             transposed.setRGB(y, x, c);
         }
     }
     return transposed;
 }
    
     public void removeHorizontalSeam(int[] seam){
          if(seam==null) throw new IllegalArgumentException("Null Horizontal Seam");
          validateHorizontalSeam(seam);
          Picture newPicture = new Picture(width(), height() - 1);

          for (int x = 0; x < width(); x++) {
              int currentY = 0;
              for (int y = 0; y < height(); y++) {
                  if (seam[x] != y) {
                      newPicture.setRGB(x, currentY, this.picture.getRGB(x, y));
                      currentY++;
                  }
              }
          }
          this.picture = newPicture;
          rebuildEnergyMapAfterH(seam);

    }

     private void rebuildEnergyMapAfterH(int[] seam) {

          double[][] newEnergyMap = new double[height()][width()];

          for(int col=0;col<energyMap[0].length;col++){
               for(int row =0,i=0 ; row< energyMap.length ; row++){
                    int deletedKaRowNumber = seam[col];
                    
                    if(row==deletedKaRowNumber) continue;
                    else if( Math.abs(row-deletedKaRowNumber)<=1 ){
                         newEnergyMap[i][col]=calcEnergyAtPixel(col,i);
                         i++;
                    }else{
                         newEnergyMap[i][col]= energyMap[row][col];
                         i++;
                    }
                    
               }
          }
          this.energyMap=newEnergyMap;
     }

     private void validateHorizontalSeam(int[] seam) {

          if(seam.length != width()) throwIllegalException("Horizontal length seam should be equal to "+width()+": "+seam.length);

          for(int i=0;i< seam.length; i++){
               int each = seam[i];
               if(!inHorizontalRange(each)) throwIllegalException("Horizontal length seam out of Range: "+each);

               int prevIndex = (i-1<0)? 0 : i-1;
               int nextIndex = (i+1>= width())?  width()-1: i+1; 
               int diff1 = seam[prevIndex] - seam[i];
               int diff2 = seam[nextIndex] -seam[i]; 
               if(!(Math.abs(diff1)<2) || !(Math.abs(diff2)<2)) throwIllegalException("Adjancent distances in seam are not less than 2");
          }
          
          
}
     
     private boolean inHorizontalRange(int i) {
     return 0<=i && i<=height()-1;
}

     public void removeVerticalSeam(int[] seam){
          this.picture= transpose(this.picture);
          this.energyMap = transposeMatrix(energyMap);
          removeHorizontalSeam(seam);
          this.energyMap=transposeMatrix(energyMap);
          this.picture = transpose(picture);
     }

     public static void main(String[] args){
          //   Picture s   = new Picture("https://coursera.cs.princeton.edu/algs4/assignments/seam/files/6x5.png");
          // // // // Picture s = new Picture(3,4);
          //  SeamCarver x = new SeamCarver(s);
          // // // // x.printCurrEnergyMap();
          //    System.out.println(Arrays.toString(x.findVerticalSeam())); 
          //    x.printCurrEnergyMap();
          //   System.out.println( x.calcEnergyAtPixel(1, 2));
          //        x.removeVerticalSeam(x.findVerticalSeam());
          //        x.printCurrEnergyMap();
          //        System.out.println(x.energy(1, 2));
          //       System.out.println( x.calcEnergyAtPixel(1, 2));
          
     }
     
     private boolean arraysAreEqual(double[][] array1, double[][] array2) {

          if(array1==null || array2==null) return false;

          if (array1.length != array2.length || array1[0].length != array2[0].length) {
              return false; // Arrays have different dimensions
          }
      
          for (int i = 0; i < array1.length; i++) {
              for (int j = 0; j < array1[0].length; j++) {
                  if (array1[i][j]!=(array2[i][j])) {
                      return false; // Elements at position (i, j) are not equal
                  }
              }
          }
      
          return true; // All elements are equal
      }
      
}