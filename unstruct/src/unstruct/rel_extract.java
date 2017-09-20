

package unstruct;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.*;
import java.util.*;

public class rel_extract {
    
    void op_extract()throws IOException{
        
        String line,subject=null,verb=null,object=null;
        int ob=0,sub=0;
        List<String> sen=new ArrayList<String>();
        List<String> relop=new ArrayList<String>();
        FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\compound.txt");
        BufferedReader br=new BufferedReader(fr);
        
        FileWriter fw= new FileWriter("D:\\RESEARCH\\new program files\\relation.txt");
        BufferedWriter bw=new BufferedWriter(fw);
        
        FileWriter fw1= new FileWriter("D:\\RESEARCH\\new program files\\operation.txt");
        BufferedWriter bw1=new BufferedWriter(fw1);
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
                if(st[j].contains("nsubj("))
                {  
                    verb=st[j].split("\\(")[1].split("-")[0];
                    subject=st[j+1].split("-")[0];
                    for(int k=j+1;k<tokenno;k++)
                    {
                        if(st[k].contains("dobj"))
                        {  
                            if(verb.equalsIgnoreCase(st[k].split("\\(")[1].split("-")[0]))
                            {
                                object=st[k+1].split("-")[0];
                                relop.add(subject+"-"+verb+"-"+object);
                             //   System.out.println(subject+"-"+verb+"-"+object);
                                ob=1;
                            }
                        }
                        else
                        if(st[k].contains("nmod:")&& st[k].contains(verb))
                        {
                            object=st[k+1].split("-")[0];
                            relop.add(subject+"-"+verb+"_"+ st[k].split(":")[1].split("\\(")[0]+"_"+object);
                           // System.out.println(subject+"-"+verb+"_"+ st[k].split(":")[1].split("\\(")[0]+"_"+object);
                            ob=1;
                        }
//                        else
//                        if(st[k].contains("nsubj:xsubj("))
//                        {
//                            object=st[k].split("\\(")[1].split("-")[0];
//                            relop.add(subject+"-"+verb+"-"+object);
//                            ob=1;
//                        }
                        
                    }
                }
                if(st[j].contains("nsubjpass("))
                {  
                    verb=st[j].split("\\(")[1].split("-")[0];
                    subject=st[j+1].split("-")[0];
                    
                    for(int k=j+1;k<tokenno;k++)
                    {
                        if(st[k].contains("case(") && st[k+1].contains("by-"))
                        {  
                                object=st[k].split("\\(")[1].split("-")[0]; 
                                relop.add(subject+"-"+verb+"_by"+"-"+object);
                                ob=1;   
                        }     
                        if(st[k].contains("case(") && st[k+1].contains("to-"))
                        {  
                                object=st[k].split("\\(")[1].split("-")[0]; 
                                relop.add(subject+"-"+verb+"_to"+"-"+object);
                                ob=1;   
                        } 
                        if(st[k].contains("dobj("))
                        {  
                                object=st[k+1].split("-")[0];
                                relop.add(subject+"-"+verb+"-"+object);
                            //    System.out.println(subject+"-"+verb+"-"+object);
                                ob=1;   
                        } 
                        else
                        if(st[k].contains("nmod:agent("))
                        {  
                               object=st[k+1].split("-")[0];
                                relop.add(subject+"-"+verb+"-"+object);
                                ob=1;   
                        } 
                    }                   
                } 
//                if(st[j].contains("nmod:of("))
//                {
//                    relop.add(st[j].split("\\(")[1].split("-")[0]+"-of-"+st[j+1].split("-")[0]);
//                }
            }
//            if(ob==0 && subject!=null && verb!=null)
//            {
//                relop.add(subject+"-"+verb);
//            }
            ob=0;
            subject=null;
            verb=null;
            object=null;
        }
        String class1=null,class2=null;
        int c1=0,c2=0;
        FileReader fr1 = new FileReader("D:\\RESEARCH\\new program files\\class.txt");
        BufferedReader br1=new BufferedReader(fr1);
        sen.clear();
        while((line=br1.readLine())!= null)
        {
            sen.add(line);
           // System.out.println(line);
        } 
        fr1.close();
        br1.close();
         Properties props = new Properties(); 
        props.put("annotators", "tokenize, ssplit, pos, lemma"); 
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
        for(int i=0;i<relop.size();i++)
        {     
         System.out.println(relop.get(i));
            class1=class2="";
            class1=relop.get(i).split("-")[0];
            if((relop.get(i).split("-",-1).length-1)>1)
                class2=relop.get(i).split("-")[2];
             Annotation document = pipeline.process(class1.replace("_", ","));
             String full="";
                for(CoreLabel token: document.get(CoreAnnotations.TokensAnnotation.class))
                {   
                    String word = token.get(CoreAnnotations.TextAnnotation.class);                
                    String lemma = token.get(CoreAnnotations.LemmaAnnotation.class); 
                    full=full+lemma;
                }
                class1=(full.replace(",","_"));  
                full="";
                if(class2!=null)
                {
                    document = pipeline.process(class2.replace("_", ","));  
                    for(CoreLabel token: document.get(CoreAnnotations.TokensAnnotation.class))
                    {   
                        String word = token.get(CoreAnnotations.TextAnnotation.class);                
                        String lemma = token.get(CoreAnnotations.LemmaAnnotation.class); 
                         full=full+lemma; 
                    }
                    class2=(full.replace(",","_")); 
                }  
              //System.out.println(class1+"........."+class2);
            for(int j=0;j<sen.size();j++)
            {
             //  System.out.println(sen.get(j));
                if(class1.equalsIgnoreCase(sen.get(j)) && class1!=null)
                {
                    c1=1;
                }
                if( class2!=null && class2.equalsIgnoreCase(sen.get(j)) )
                {
                    c2=1;
                } 
            }
            System.out.println(c1+"*******"+c2);
            if(c1==1 && c2==1 )
            {               
                bw.write(relop.get(i));
              //  System.out.println(relop.get(i));
                bw.newLine();
            }
            else if(c1==1)
            {
                bw1.write(relop.get(i));
              // System.out.println(relop.get(i));
                bw1.newLine();
            }
            else if(class2=="" || (c1==1 && relop.get(i).split("-")[1].split("-")[0].contains("enter")))
            {
                bw1.write(relop.get(i));
                bw1.newLine();
            }
            c1=0;
            c2=0;
        }
        
        bw.close();
        fw.close();
        
        bw1.close();
        fw1.close();
    }
    
    void generalization () throws IOException
    {
        String line;
        String c1=null;
        List<String> sen=new ArrayList<String>();
        FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\compound.txt");
        BufferedReader br=new BufferedReader(fr);
        
        FileWriter fw= new FileWriter("D:\\RESEARCH\\new program files\\generalization.txt");
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
                if(st[j].contains("types-")|| st[j].contains("kinds-"))
                { 
                    for(j=0;j<tokenno;j++)
                    {
                         if(st[j].contains("case(") && st[j+1].contains("of-"))
                        { 
                           c1=st[j].split("\\(")[1].split("-")[0];
                        }
                         if(st[j].contains("nmod:of(types-"))
                         {
                             if(!st[j+1].split("-")[0].contains(c1))
                             {
                                 bw.write(c1+"-types_of-"+st[j+1].split("-")[0]);
                                 bw.newLine();
                             }
                              
                         }
                    }
                }
            }
        }
        bw.close();
        fw.close();
        fr.close();
        br.close();
    }
}

