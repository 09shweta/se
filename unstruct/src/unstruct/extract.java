/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unstruct;

import java.io.*;
import java.util.*;
public class extract {   
    //attributes selection on the basis of "has"
    void extract_attributes() throws  IOException
    {
        String line;
        List<String> sen=new ArrayList<String>();
        FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\compound.txt");
        BufferedReader br=new BufferedReader(fr);
        FileWriter fw1= new FileWriter("D:\\RESEARCH\\new program files\\class.txt",true);
        BufferedWriter bw1=new BufferedWriter(fw1);
        FileWriter fw= new FileWriter("D:\\RESEARCH\\new program files\\has_attributes.txt");
        BufferedWriter bw=new BufferedWriter(fw);
        while((line=br.readLine())!= null)
        {
            sen.add(line);
        } 
        fr.close();
        br.close();
        for(int i=0;i<sen.size();i++)
        {
            StringTokenizer token=new StringTokenizer(sen.get(i));
            int tokenno=token.countTokens();
            String[] st= new String[tokenno];
            for( int j=0;j<tokenno;j++)
                  st[j]="";
            int j=0;
            while(token.hasMoreTokens()){
               st[j]=token.nextToken();              
               j++;
            }          
            String subject=null;
            for(j=0;j<tokenno;j++)
            {
                if(st[j].contains("nsubj("))
                {
                    subject=st[j+1].split("-")[0];
                }
                if(st[j].contains("nsubj(have-") || st[j].contains("nsubj(has-") || st[j].contains("nsubj(own-")|| st[j].contains("nsubj(possess-")|| st[j].contains("nsubj(holds-"))
                {
                   int k=j;
                   while(!st[k].contains(")"))
                       k++;
                   bw.write(st[k].split("-")[0]);
                   bw.write("-");
                   
                   k=j;
                   while(!st[k].contains("dobj("))
                       k++;
                   k=k+1;
                   bw.write(st[k].split("-")[0]);
                   bw.newLine();
                }
                if(st[j].contains("nmod:poss(") && st[j+2].contains("case(") &&  st[j+3].contains("'s-"))
                {
                    bw.write(st[j+1].split("-")[0]+"-"+st[j].split("\\(")[1].split("-")[0]);
                    bw.newLine();
                    bw1.write(st[j+1].split("-")[0]);
                    bw1.newLine();
                }
                if(st[j].contains("nummod(") && !st[j+1].split("-")[0].equalsIgnoreCase("one"))
                {
                    bw.write(subject+"-"+"number"+"_"+st[j].split("\\(")[1].split("-")[0]);
                    bw.newLine();
                }
            }
        }
        bw.close();
        fw.close();
        bw1.close();
        fw1.close();
    }
    
    void identified_attributes() throws  IOException
    {
        String line;
        List<String> sen=new ArrayList<String>();
        FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\compound.txt");
        BufferedReader br=new BufferedReader(fr);
        
        FileWriter fw1= new FileWriter("D:\\RESEARCH\\new program files\\class.txt",true);
        BufferedWriter bw1=new BufferedWriter(fw1);
        
        FileWriter fw= new FileWriter("D:\\RESEARCH\\new program files\\identified_attributes.txt");
        BufferedWriter bw=new BufferedWriter(fw);
        while((line=br.readLine())!= null)
        {
            sen.add(line);
        } 
        fr.close();
        br.close();
        int k;
        for(int i=0;i<sen.size();i++)
        {
            StringTokenizer token=new StringTokenizer(sen.get(i));
            int tokenno=token.countTokens();
            String[] st= new String[tokenno];
            for( int j=0;j<tokenno;j++)
                  st[j]="";
            int j=0;
            while(token.hasMoreTokens()){
               st[j]=token.nextToken();              
               j++;
            }                
            for(j=0;j<tokenno;j++)
            {
                k=0;
                if(st[j].contains("nsubjpass(identified-") || st[j].contains("nsubjpass(denoted-")|| st[j].contains("nsubjpass(recognized-"))
                {
                        bw1.append(st[j+1].split("-")[0]);
                      //  System.out.println(st[j+1].split("-")[0]);
                        bw1.newLine();
                        bw.write(st[j+1].split("-")[0]+"-");
                    k=j;    
                   while(!st[k].contains("nmod:"))
                   {
                        k++;
                   }
                        bw.write(st[k+1].split("-")[0]);
                        bw.newLine();
                }
                
            }
        }
        bw.close();
        fw.close();
        bw1.close();
        fw1.close();
    }
    
    void enter_attributes() throws  IOException
    {
        String line,subject=null;
        List<String> sen=new ArrayList<String>();
        List<String> sen1=new ArrayList<String>();
         List<String> sen2=new ArrayList<String>();
        FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\compound.txt");
        BufferedReader br=new BufferedReader(fr);
        FileReader fr1= new FileReader("D:\\RESEARCH\\new program files\\class.txt");
        BufferedReader br1=new BufferedReader(fr1);
        FileWriter fw= new FileWriter("D:\\RESEARCH\\new program files\\enter_attributes.txt");
        BufferedWriter bw=new BufferedWriter(fw);
        
        
        while((line=br.readLine())!= null)
        {
            sen.add(line);
        } 
        fr.close();
        br.close();
        fr = new FileReader("D:\\RESEARCH\\new program files\\noun.txt");
        br=new BufferedReader(fr);
        while((line=br.readLine())!= null)
        {
            sen1.add(line);
        } 
        while((line=br1.readLine())!= null)
        {
            sen2.add(line);
            //System.out.println(line);
        } 
        
        for(int i=0;i<sen.size();i++)
        {
            StringTokenizer token=new StringTokenizer(sen.get(i));
            int tokenno=token.countTokens();
            String[] st= new String[tokenno];
            for( int j=0;j<tokenno;j++)
                  st[j]="";
            int j=0;
            while(token.hasMoreTokens()){
               st[j]=token.nextToken();              
               j++;
            }                   
            for(j=0;j<tokenno;j++)
            {
                if(st[j].contains("nsubj(type-") || st[j].contains("nsubj(enter-")||st[j].contains("nsubj(input-"))
                {                                     
                   subject=st[j+1].split("-")[0];
                }
                if(st[j].contains("dobj(type-") || st[j].contains("dobj(enter-")||st[j].contains("dobj(input-"))
                {       
                   if(st[j+1].contains("_"))
                   {
                       for(int k=0;k<sen1.size();k++)
                       {
                            if(st[j+1].split("_")[0].equalsIgnoreCase(sen1.get(k)))
                            {
                                //sen.add(st[j+1].split("_")[0]);                      
                                bw.write(st[j+1].split("_")[0]+"-"+st[j+1].split("_")[1].split("-")[0]);
                                bw.newLine();
                                sen2.add(st[j+1].split("_")[0]);
                            }
                       }
                   }
                   else
                   {     
                      // bw.write(subject+"-"+st[j].split("\\(")[1].split("-")[0]+"_");
                       bw.write(subject+"-"+st[j+1].split("-")[0]);
                       bw.newLine();
                       sen2.add(subject);
                   }
                   for(int k=0;k<sen2.size();k++)
                   {
                        if(st[j+1].split("-")[0].equalsIgnoreCase(sen2.get(k)))
                        {
                                 sen2.remove(k);
                       }
                   }
                }               
            }
        }
        bw.close();
        fw.close();
        FileWriter fw1= new FileWriter("D:\\RESEARCH\\new program files\\class.txt",true);
        BufferedWriter bw1=new BufferedWriter(fw1);
        for(int k=0;k<sen2.size();k++)
       {
           bw1.write(sen2.get(k));
          // System.out.println(sen2.get(k));
           bw1.newLine();
       }
        
        bw1.close();
        fw1.close();
        fr.close();
        br.close();
    }
    void acc_attributes() throws IOException
    {
         String line;
        List<String> sen=new ArrayList<String>();
        FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\compound.txt");
        BufferedReader br=new BufferedReader(fr);
        
        FileWriter fw= new FileWriter("D:\\RESEARCH\\new program files\\acc_attributes.txt");
        BufferedWriter bw=new BufferedWriter(fw);
        while((line=br.readLine())!= null)
        {
            sen.add(line);
        } 
        fr.close();
        br.close();
        for(int i=0;i<sen.size();i++)
        {
            StringTokenizer token=new StringTokenizer(sen.get(i));
            int tokenno=token.countTokens();
            String[] st= new String[tokenno];
            for( int j=0;j<tokenno;j++)
                  st[j]="";
            int j=0;
            while(token.hasMoreTokens()){
               st[j]=token.nextToken();              
               j++;
            }                   
            for(j=0;j<tokenno;j++)
            {
                if(st[j].contains("nsubj"))
                {                
                   for(int k=j;k<tokenno;k++)
                    {
                        if(st[k].contains("nmod:according_to("))
                        {
                            bw.write(st[j+1].split("-")[0]+"-"+st[k+1].split("-")[0]);
                            bw.newLine();
                        }
                    }
                   break;
                }
            }
        }
        bw.close();
        fw.close();
    }
    
     void their_attributes() throws IOException
    {
         String line;
        List<String> sen=new ArrayList<String>();
         FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\compound.txt");
        BufferedReader br=new BufferedReader(fr);
        
        FileWriter fw= new FileWriter("D:\\RESEARCH\\new program files\\their_attributes.txt");
        BufferedWriter bw=new BufferedWriter(fw);
        while((line=br.readLine())!= null)
        {
            sen.add(line);
        } 
        fr.close();
        br.close();
        for(int i=0;i<sen.size();i++)
        {
            StringTokenizer token=new StringTokenizer(sen.get(i));
            int tokenno=token.countTokens();
            String[] st= new String[tokenno];
            for( int j=0;j<tokenno;j++)
                  st[j]="";
            int j=0;
            while(token.hasMoreTokens()){
               st[j]=token.nextToken();              
               j++;
            }                   
            for(j=0;j<tokenno;j++)
            {
                if(st[j].contains("nsubj"))
                {                
                   for(int k=j;k<tokenno;k++)
                    {
                         if(st[k].contains("nmod:poss("))
                        {
                            bw.write(st[j+1].split("-")[0]+"-"+st[k].substring(10,st[k].length()-4));
                            bw.newLine();
                            
                        }
                    }
                   break; 
                }
            }
        }
        bw.close();
        fw.close();
    }
     void foron_including() throws IOException
     {
        String line;
        List<String> sen=new ArrayList<String>();
         FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\compound.txt");
        BufferedReader br=new BufferedReader(fr);
        
        FileWriter fw= new FileWriter("D:\\RESEARCH\\new program files\\onfor_attributes.txt");
        BufferedWriter bw=new BufferedWriter(fw);
        while((line=br.readLine())!= null)
        {
            sen.add(line);
        } 
        fr.close();
        br.close();
        
        for(int i=0;i<sen.size();i++)
        {
            StringTokenizer token=new StringTokenizer(sen.get(i));
            int tokenno=token.countTokens();
            String[] st= new String[tokenno];
            for( int j=0;j<tokenno;j++)
                  st[j]="";
            int j=0;
            while(token.hasMoreTokens()){
               st[j]=token.nextToken(); 
           //   System.out.println(st[j]);
               j++;
            }          
            String subject=null;
            String object=null;
            for(j=0;j<tokenno;j++)
            {
                if(st[j].contains("nsubj("))
                    subject=st[j+1].split("-")[0];
                if(st[j].contains("dobj("))
                    object=st[j+1].split("-")[0];
                
                if(st[j].contains("case(") && st[j+1].contains("including-"))
                {
                    bw.write(subject+"-"+st[j].substring(st[j].indexOf("(")+1, st[j].indexOf("-")));                 
                    bw.newLine();
                }
                 if(st[j].contains("case(") && (st[j+1].contains("on-")|| st[j+1].contains("for-")))
                {
                    bw.write(object+"-"+st[j].substring(st[j].indexOf("(")+1, st[j].indexOf("-")));                 
                    bw.newLine();
                }
                
            }
        }
        bw.close();
        fw.close();
     } 
//     void rel_extract()throws IOException
//     {
//         FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\compound.txt");
//         BufferedReader br=new BufferedReader(fr);
//         FileWriter fw= new FileWriter("D:\\RESEARCH\\new program files\\rel_extract.txt", true);
//         BufferedWriter bw=new BufferedWriter(fw);
//         String line;
//         List<String> sen=new ArrayList<String>();
//         while((line=br.readLine())!= null)
//        {
//            sen.add(line);
//        } 
//        fr.close();
//        br.close();
//        
//        for(int i=0;i<sen.size();i++)
//        {
//            StringTokenizer token=new StringTokenizer(sen.get(i));
//            int tokenno=token.countTokens();
//            String[] st= new String[tokenno];
//            String t1=null;
//            String[][] rel;
//            for( int j=0;j<tokenno;j++)
//                  st[j]="";
//            int j=0;
//            while(token.hasMoreTokens()){
//               st[j]=token.nextToken(); 
//               j++;
//            }                   
//            for(j=0;j<tokenno;j++)
//            {
//                 if(st[j].contains("compound("))
//                {
//                    
//                     t1= st[j+1].split("-")[0].toLowerCase();
//                     t1=t1+st[j].split("\\(")[1].split("-")[0].toLowerCase();
//                     
//                }
//                 if(st[j].contains("nsubj"))
//                {
//                      bw.newLine();
//                    if (t1==null)
//                    bw.write( st[j+1].split("-")[0]);                                        
//                    else
//                    {
//                        bw.write(t1);
//                        t1=null;
//                    }
//                    bw.write('-');                  
//                }
//                 if(st[j].contains("dobj("))
//                {
//                    bw.write(st[j].split("\\(")[1].split("-")[0]);                 
//                    bw.write('-');
//                    
//                    if (t1==null)
//                        bw.write(st[j+1].split("-")[0]);
//                    else
//                    {
//                        bw.write(t1);
//                        t1=null;
//                    }
//                  
//                }
//                 
//            }
//        }
//        bw.close();
//        fw.close();    
//    }
     void poss_verb_attributes() throws  IOException
    {
        String line,subject=null;
        List<String> sen=new ArrayList<String>();
        FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\compound.txt");
        BufferedReader br=new BufferedReader(fr);
        
        FileWriter fw= new FileWriter("D:\\RESEARCH\\new program files\\has_attributes.txt",true);
        BufferedWriter bw=new BufferedWriter(fw);
        while((line=br.readLine())!= null)
        {
            sen.add(line);
        } 
        fr.close();
        br.close();
        for(int i=0;i<sen.size();i++)
        {
            StringTokenizer token=new StringTokenizer(sen.get(i));
            int tokenno=token.countTokens();
            String[] st= new String[tokenno];
            for( int j=0;j<tokenno;j++)
                  st[j]="";
            int j=0;
            while(token.hasMoreTokens()){
               st[j]=token.nextToken();              
               j++;
            }                   
            for(j=0;j<tokenno;j++)
            {     
                if(st[j].contains("nsubj") && subject==null )
                    subject=st[j+1].split("-")[0];
                if(st[j].contains("nmod:poss(") && (st[j+1].contains("their-") || st[j+1].contains("his-")|| st[j+1].contains("her-")|| st[j+1].contains("its-")|| st[j+1].contains("our-")) )
                {
                      bw.write(subject+"-"+st[j].split("\\(")[1].split("-")[0]);
                     // System.out.println(subject+"-"+st[j].split("\\(")[1].split("-")[0]);
                }
            }
            subject=null;
        }
        bw.close();
        fw.close();
    }
     
}