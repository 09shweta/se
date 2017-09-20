
package unstruct;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.*;
import java.util.*;

public class spearate {
    
    void sparate_class() throws IOException
    {
        String line;
        List<String> class1=new ArrayList<String>();
        List<String> class2=new ArrayList<String>();
        Properties props = new Properties(); 
        props.put("annotators", "tokenize, ssplit, pos, lemma"); 
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
        
        FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\class.txt") ;
        BufferedReader br=new BufferedReader(fr);
        
        while((line=br.readLine())!= null)
        {
            class1.add(line);
        }
         fr.close();
         br.close();
         
        List<String> operation=new ArrayList<String>();
        List<String> operation1=new ArrayList<String>();
        fr = new FileReader("D:\\RESEARCH\\new program files\\operation.txt") ;
        br=new BufferedReader(fr);
        line="";
        while((line=br.readLine())!= null)
        {
            operation.add(line);
        }
         fr.close();
         br.close();
         for(int i=0;i<operation.size();i++)
        {     
            String c1=operation.get(i).split("-")[0];
            String rel="";
            Annotation document = pipeline.process(c1.replace("_", ","));  
                for(CoreLabel token: document.get(CoreAnnotations.TokensAnnotation.class))
                {               
                    String lemma = token.get(CoreAnnotations.LemmaAnnotation.class); 
                    rel=rel+lemma;
                }
                c1=rel.replace(",", "_");
                operation.set(i,operation.get(i).replace(operation.get(i).split("-")[0], c1));
              //  System.out.println(operation.get(i));
        }
          
         List<String> relation=new ArrayList<String>();
         List<String> relation1=new ArrayList<String>();
         fr = new FileReader("D:\\RESEARCH\\new program files\\relation.txt") ;
        br=new BufferedReader(fr);
       
        while((line=br.readLine())!= null)
        {
            relation.add(line);
        }
         fr.close();
         br.close();
         
        fr = new FileReader("D:\\RESEARCH\\new program files\\attribute.txt") ;
        br = new BufferedReader(fr);
        List<String> attribute=new ArrayList<String>();
        List<String> attribute1=new ArrayList<String>();
        while((line=br.readLine())!= null)
        {
            attribute.add(line);
        }
        fr.close();
        br.close();
        for(int i=0;i<attribute.size();i++)
        {     
            String c1=attribute.get(i).split("-")[0];
            String rel="";
            Annotation document = pipeline.process(c1.replace("_", ","));  
                for(CoreLabel token: document.get(CoreAnnotations.TokensAnnotation.class))
                {   
                    String word = token.get(CoreAnnotations.TextAnnotation.class);                
                    String lemma = token.get(CoreAnnotations.LemmaAnnotation.class); 
                    rel=rel+lemma;
                }
                c1=rel.replace(",", "_");
                attribute.set(i,c1+"-"+attribute.get(i).split("-")[1]); 
        }
        for(int i=0;i<relation.size();i++)
        {     
            String c2=null;
            String c1=relation.get(i).split("-")[0];
            if((relation.get(i).split("-",-1).length-1)>1)
             c2=relation.get(i).split("-")[2];
            String rel="";
             Annotation document = pipeline.process(c1.replace("_", ","));  
                for(CoreLabel token: document.get(CoreAnnotations.TokensAnnotation.class))
                {   
                    String word = token.get(CoreAnnotations.TextAnnotation.class);                
                    String lemma = token.get(CoreAnnotations.LemmaAnnotation.class); 
                     rel=rel+lemma;
                }
                c1=rel.replace(",", "_");
                rel="";
                if(c2!=null)
                {
                    document = pipeline.process(c2.replace("_", ","));  
                    for(CoreLabel token: document.get(CoreAnnotations.TokensAnnotation.class))
                    {   
                        String word = token.get(CoreAnnotations.TextAnnotation.class);                
                        String lemma = token.get(CoreAnnotations.LemmaAnnotation.class); 
                         rel=rel+lemma;
                    }
                    c2=rel.replace(",", "_");
                }
               relation.set(i,c1+"-"+relation.get(i).split("-")[1]+"-"+c2); 
        }
       

         for(int i=0;i<class1.size();i++)
         {
            
             if(class1.get(i).contains("system") || class1.get(i).contains("some") || class1.get(i).contains("information")|| class1.get(i).contains("one"))
             {
                 continue;
             }
             for(int j=0;j<operation.size();j++)
             {
                 if(class1.get(i).equalsIgnoreCase(operation.get(j).split("-")[0]))
                 {
                     class2.add(class1.get(i));
                    
                     break;
                 }                 
             }
            
             for(int j=0;j<relation.size();j++)
             {
                 if(class1.get(i).equalsIgnoreCase(relation.get(j).split("-")[0])||class1.get(i).equalsIgnoreCase(relation.get(j).split("-")[2]))
                 {        
                     class2.add(class1.get(i));
                    // System.out.println(class1.get(i));
                     break;
                 }
             }
             for(int j=0;j<attribute.size();j++)
             {
                 if(class1.get(i).equalsIgnoreCase(attribute.get(j).split("-")[0]))
                 {
                     class2.add(class1.get(i));
                    //  System.out.println(class1.get(i));
                     break;
                 }    
             }
             
         }
        fileread fread=new fileread();
        List<String> attnondup = new ArrayList<String>();
        attnondup=fread.indef_adj();
        for(int i=0;i<attnondup.size();i++)
        {

            class2.add(attnondup.get(i));
        }
         
        HashSet<String> dup = new HashSet<String>(class2);
        List<String> nondup = new ArrayList<String>(dup);
        FileWriter fw= new FileWriter("D:\\RESEARCH\\new program files\\class.txt");
        BufferedWriter bw=new BufferedWriter(fw);
        for(int i=0;i<nondup.size();i++)
        {
            bw.write(nondup.get(i));
          // System.out.println(nondup.get(i));
            bw.newLine();
        }
        bw.close();
        fw.close();
         fr = new FileReader("D:\\RESEARCH\\new program files\\class.txt") ;
         br=new BufferedReader(fr);
        nondup.clear();
        while((line=br.readLine())!= null)
        {
            nondup.add(line);
          // System.out.println(line);
        }
         fr.close();
         br.close();
        
        for(int i=0;i<operation.size();i++)
        {
            int f=0;
            for(int j=0;j<nondup.size();j++)
            {
                if(operation.get(i).split("-")[0].equalsIgnoreCase(nondup.get(j)) && !operation.get(i).split("-")[1].equalsIgnoreCase("has") && !operation.get(i).split("-")[1].equalsIgnoreCase("have"))
                {
                    operation1.add(operation.get(i));
                    break;
                }
                
            }
        }
        
        for(int i=0;i<relation.size();i++)
        {
            int f=0,f1f=0;
            for(int j=0;j<nondup.size();j++)
            {
                if(relation.get(i).split("-")[0].equalsIgnoreCase(nondup.get(j)))
                {  

                   f=1;
                }
                if(relation.get(i).split("-")[2].equalsIgnoreCase(nondup.get(j)))
                {
                    f1f=1;
                }
            }
            if(f==1 && f1f==1 )
                relation1.add(relation.get(i));
        }
        for(int i=0;i<attribute.size();i++)
        {
            int f=0,f1f=0;
            for(int j=0;j<nondup.size();j++)
            {
                if(attribute.get(i).split("-")[0].equalsIgnoreCase(nondup.get(j)))
                {  

                   f=1;
                }
                if(attribute.get(i).split("-")[1].equalsIgnoreCase(nondup.get(j)))
                {
                    f1f=1;
                }
            }
            if(f==1 && f1f!=1 )
                attribute1.add(attribute.get(i));
        }
        FileWriter fw1= new FileWriter("D:\\RESEARCH\\new program files\\operation.txt");
        BufferedWriter bw1=new BufferedWriter(fw1);
        FileWriter fw2= new FileWriter("D:\\RESEARCH\\new program files\\relation.txt");
        BufferedWriter bw2=new BufferedWriter(fw2);
        FileWriter fw3= new FileWriter("D:\\RESEARCH\\new program files\\attribute.txt");
        BufferedWriter bw3=new BufferedWriter(fw3);

        dup = new HashSet<String>(operation1);
        nondup = new ArrayList<String>(dup);
        for(int i=0;i<nondup.size();i++)
        {
             bw1.write(nondup.get(i));
             bw1.newLine();
        }
        dup = new HashSet<String>(relation1);
        nondup = new ArrayList<String>(dup);
           for(int i=0;i<nondup.size();i++)
        {
             bw2.write(nondup.get(i));
             bw2.newLine();
        }
           for(int i=0;i<attribute1.size();i++)
        {
             bw3.write(attribute1.get(i));
             bw3.newLine();
        }
           
 
        bw1.close();
        fw1.close();
        bw2.close();
        fw2.close();
        bw3.close();
        fw3.close();
    }
    void accumulate_attribue() throws IOException
    {
        String line;
        
        List<String> class1=new ArrayList<String>();
        FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\class.txt") ;
        BufferedReader br=new BufferedReader(fr);
        
        while((line=br.readLine())!= null)
        {
            class1.add(line);
        }
         fr.close();
         br.close();
         
        List<String> attribute=new ArrayList<String>();
        fr = new FileReader("D:\\RESEARCH\\new program files\\acc_attributes.txt") ;
        br=new BufferedReader(fr);
        
        while((line=br.readLine())!= null)
        {
            attribute.add(line);
        }
         fr.close();
         br.close();
        
         fr = new FileReader("D:\\RESEARCH\\new program files\\enter_attributes.txt") ;
        br=new BufferedReader(fr);
        
        while((line=br.readLine())!= null)
        {
            attribute.add(line);
        }
         fr.close();
         br.close();
         
         
         fr = new FileReader("D:\\RESEARCH\\new program files\\has_attributes.txt") ;
        br=new BufferedReader(fr);
        
        while((line=br.readLine())!= null)
        {
            attribute.add(line);
        }
         fr.close();
         br.close();
         
        
         fr = new FileReader("D:\\RESEARCH\\new program files\\identified_attributes.txt") ;
        br=new BufferedReader(fr);
        
        while((line=br.readLine())!= null)
        {
            attribute.add(line);
        }
         fr.close();
         br.close();
         

         fr = new FileReader("D:\\RESEARCH\\new program files\\their_attributes.txt") ;
        br=new BufferedReader(fr);
        
        while((line=br.readLine())!= null)
        {
           attribute.add(line);
        }
         fr.close();
         br.close();
         
         fr = new FileReader("D:\\RESEARCH\\new program files\\onfor_attributes.txt") ;
        br=new BufferedReader(fr);
        
        while((line=br.readLine())!= null)
        {
            attribute.add(line);
        }
         fr.close();
         br.close();
//         int f=0;
//          String lemmaatt=null;
//         Properties props = new Properties(); 
//        props.put("annotators", "tokenize, ssplit, pos, lemma"); 
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
//        for(int i=0;i<attribute.size();i++)
//        {
//            lemmaatt=null;
//            Annotation document = pipeline.process(attribute.get(i).replace("_", ",")); 
//                for(CoreLabel token: document.get(CoreAnnotations.TokensAnnotation.class))
//                {   
//                    String word = token.get(CoreAnnotations.TextAnnotation.class);                
//                    String lemma = token.get(CoreAnnotations.LemmaAnnotation.class); 
//                    if (lemmaatt==null)
//                        lemmaatt=lemma;                    
//                    else
//                        lemmaatt=lemmaatt+lemma;
//                }
//                attribute.set(i, lemmaatt.replace(",", "_"));
//        }
//        List<String> att=new ArrayList<String>();
//        for(int i=0;i<attribute.size();i++)
//        {
//            for(int j=0;j<class1.size();j++)
//            {           
//                if(attribute.get(i).split("-")[0].equalsIgnoreCase(class1.get(j)) && attribute.get(i).split("-")[0]!=null)
//                {    
//                    att.add(attribute.get(i));
//                }
//            }
//        }
         HashSet<String> dup = new HashSet<String>(attribute);
        List<String> attribute1 = new ArrayList<String>(dup);
        FileWriter fw= new FileWriter("D:\\RESEARCH\\new program files\\attribute.txt");
        BufferedWriter bw=new BufferedWriter(fw);
      
        for(int i=0;i<attribute1.size();i++)
        {
            bw.write(attribute1.get(i));
            bw.newLine();
        }
        bw.close();
        fw.close();
    }
}
