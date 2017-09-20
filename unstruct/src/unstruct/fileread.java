
package unstruct;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.util.regex.*; 
import java.io.*;
import java.util.*;
public class fileread {
/*method for seprating each senetnce and save it in file PARSED*/
    void compound() throws FileNotFoundException, IOException
    {
        FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\dependency.txt");
        BufferedReader br=new BufferedReader(fr);
      
        FileWriter fw= new FileWriter("D:\\RESEARCH\\new program files\\compound.txt");
        BufferedWriter bw=new BufferedWriter(fw);
        
        String line,c1=null,c2=null;
        List<String> sen=new ArrayList<String>();
        while((line=br.readLine())!= null)
        {         
            sen.add(line);
        } 
        int n=0;
         for(int i=0;i<sen.size();i++)
        {
            c1=null;
            c2=null;
            n=0;
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
                if(st[j].contains("amod(") && !st[j+1].contains("many") && !st[j+1].contains("specific"))
                {
                    c1=st[j].split("\\(")[1].split("-")[0];
                    c2=st[j+1].split("-")[0];
                }
                if(st[j].contains("compound("))
                {
                    if(n>0)
                    {
                        c1=null;
                        c2=null;
                    }
                    c1=st[j].split("\\(")[1].split("-")[0];
                    if (c2 == null)
                        c2=st[j+1].split("-")[0];
                    else
                        c2=c2+"_"+st[j+1].split("-")[0];
                    n++;
                }
                if(st[j].contains(c1+"-"))
                {
                    st[j]=st[j].replace(c1, c2+"_"+c1);
                    bw.write(st[j]);     
                }
                else
                    bw.write(st[j]);    
                bw.append(" ");
            }
            bw.newLine();
        }
        fr.close();
        br.close();
        bw.close();
        fw.close();
    }
    
     List<String> file_read() throws FileNotFoundException, IOException
    {
        String line;
        int count=0;
        List<String> sentences = new ArrayList<String>();
        FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\parsed.txt");
        BufferedReader br=new BufferedReader(fr);
        sentences.add("");
        while((line=br.readLine())!= null)
        {
            if(line.contains("(ROOT"))
            {
                if(!( sentences.get(0)==""))
                {
                     sentences.add("");
                    count++;
                }
            }
            sentences.set(count, sentences.get(count)+line);

        } 
        fr.close();
        br.close();
        return sentences;
    }
            
   /* method for extracting noun phrase and firstly store it into list SEN then into NOUN_PHRASE file*/ 
    List<String> noun_phrase(String []sentence,int totalsen) throws IOException
    {
        List<String> sen = new ArrayList<String>();
        int count=0;
        FileWriter fw = new FileWriter("D:\\RESEARCH\\new program files\\noun_phrase.txt") ;
        BufferedWriter bw=new BufferedWriter(fw);
        int tokenno;
        for(int i=0;i<totalsen;i++)
        {
            StringTokenizer token=new StringTokenizer(sentence[i]);
            tokenno=token.countTokens();
            String[] st= new String[tokenno];
            for( int j=0;j<tokenno;j++)
                  st[j]="";
            int j=0;
            while(token.hasMoreTokens()){
               st[j]=token.nextToken();
              
               j++;
            }
            int para=0;
            sen.add("");
            for(j=0;j<tokenno;j++)
            {
                if(st[j].contains("(NP"))
                {
                    count++;
                    sen.add("");                  
                    sen.set(count,sen.get(count)+" "+st[j]);
                    para=0;
                    for(int k=j+1;k<tokenno;k++)
                    {
                        sen.set(count,sen.get(count)+" "+st[k]);
                        if(st[k].contains("("))
                        {   
                            int counter = st[k].split("\\(",Integer.MAX_VALUE).length - 1;
                            para=para+counter;  
                            
                        }
                        else
                        if(st[k].contains(")"))
                        {   
                            int counter = st[k].split("\\)",Integer.MAX_VALUE).length - 1;
                            para=para-counter;   
                        }
                        if(para<=-1)
                             break;                      
                    }
                }
            }
        }
       //removing blank element and removing recursive NP phrases in the sentences
        for(int j=0;j<sen.size();j++)
        {
          sen.remove("");
          if(sen.get(j).lastIndexOf("(NP")>=2)
              sen.remove(j);
        }   
        
        //storing noun phrases in the file noun_phrase
        for(int j=0;j<sen.size();j++)
        {
            bw.write(sen.get(j));
            bw.newLine();
        }
        bw.close();
        fw.close();
       //storing from the noun from noun phrase 
        int k=0;
        List<String> noun = new ArrayList<String>();
        for(int i=0;i<sen.size();i++)
        {
            noun.add("");
            StringTokenizer token=new StringTokenizer(sen.get(i));
            tokenno=token.countTokens();
            String[] st= new String[tokenno];
            for( int j=0;j<tokenno;j++)
                  st[j]="";
            int j=0;
            while(token.hasMoreTokens()){
               st[j]=token.nextToken();              
               j++;
            }
            //storing the noun into noun list
          
            for(j=0;j<tokenno;j++)
            {
                if(st[j].contains("(CC"))
                {
                    noun.add("");
                    k++;
                }
                else
                if(st[j].contains("("))
                {
                    if(!st[j].contains("(NP") && !st[j].contains("(DT") )  //removing s from the plural                              
                   noun.set(k,noun.get(k)+st[j+1].split("\\)")[0].toLowerCase()+"_"); 
                }
            }        
            k++;
        }
        Properties props = new Properties(); 
        props.put("annotators", "tokenize, ssplit, pos, lemma"); 
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
        //removing duplicate noun
        HashSet<String> dup = new HashSet<String>(noun);
        List<String> nondup = new ArrayList<String>(dup);
       //storing the noun in the NOUN file 
        FileWriter fw1 = new FileWriter("D:\\RESEARCH\\new program files\\noun.txt") ;
        BufferedWriter bw1=new BufferedWriter(fw1);
        for(int i=0;i<nondup.size();i++)
        {
            int f=0;
            Annotation document = pipeline.process(nondup.get(i).replace("_", "-"));  
            for(CoreLabel token: document.get(CoreAnnotations.TokensAnnotation.class))
            {   
                String word = token.get(CoreAnnotations.TextAnnotation.class);                
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class); 
                nondup.set(i,lemma.replace("-","_"));  
            
            if(nondup.get(i).length()>2)
            {                 
                bw1.write(nondup.get(i));
                bw1.newLine();
            }    
            }
        }
        bw1.close();
        fw1.close();
        return(nondup);     
    }
    void subject() throws  IOException
    {
        String line;
        List<String> sub=new ArrayList<String>();
        List<String> sen=new ArrayList<String>();
        FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\compound.txt");
        BufferedReader br=new BufferedReader(fr);
         while((line=br.readLine())!= null)
        {
            sen.add(line);
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
           String t1="";
            for(j=0;j<tokenno;j++)
            {                
                if(st[j].contains("nsubj(") || (st[j].contains("agent(")))            
                {                  
                    if(st[j].contains("nmod:agent(identified-")||st[j].contains("nmod:agent(denoted-"))
                        continue;
                    sub.add(st[j+1].split("\\-")[0].toLowerCase());
                    //System.out.println(st[j+1].split("\\-")[0].toLowerCase());
                }      
                if(st[j].contains("csubj(") )            
                {                   
                   sub.add("to"+"_"+st[j+1].split("\\-")[0].toLowerCase());
                } 
            }
        }
        
        FileWriter fw = new FileWriter("D:\\RESEARCH\\new program files\\class.txt") ;
        BufferedWriter bw=new BufferedWriter(fw);
        
        HashSet<String> attdup = new HashSet<String>(sub);
        List<String> attnondup = new ArrayList<String>(attdup); 
        
        for(int i=0;i<attnondup.size();i++)
        {
            bw.write(attnondup.get(i));
           // System.out.println(attnondup.get(i));
            bw.newLine();
        }
        
        bw.close();
        fw.close();
        
        br.close();
        fr.close();
    }
    
    void predicate_noun() throws IOException
    {

        String line,sub=null,sub1=null;
        int flag=0,casee=0;
        List<String> pred=new ArrayList<String>();
        List<String> sen=new ArrayList<String>();
        List<String> predclass=new ArrayList<String>();
        List<String> noun_phrase=new ArrayList<String>();
        FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\compound.txt");
        BufferedReader br=new BufferedReader(fr);
        
        FileWriter fw = new FileWriter("D:\\RESEARCH\\new program files\\class.txt", true) ;
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
           String t1="";casee=0;
            for(j=0;j<tokenno;j++)
            {                
                if(st[j].contains("nsubj("))            
                {
                    sub=st[j+1].split("\\-")[0].toLowerCase();
                    sub1=st[j].split("\\(")[1].split("-")[0].toLowerCase();
                }   
               else
               if(st[j].contains("cop(") )            
                {
                    for(int k=j+1;k<tokenno;k++)
                    {
                        if(st[k].contains("case("))
                        {
                            casee=1;
                            break;
                        }
                    }    
                    if(casee==0)
                        predclass.add(sub1);
//                   bw.write(sub1);
//                   bw.newLine();
                } 
                
            }  
            sub=null;
            sub1=null;
        }        
        sen.clear();
        fr = new FileReader("D:\\RESEARCH\\new program files\\noun.txt");
        br=new BufferedReader(fr);
        while((line=br.readLine())!= null)
        {
            sen.add(line);
        } 
         for(int i=0;i<predclass.size();i++)
        {
            for(int j=0;j<sen.size();j++)
            {
                if(predclass.get(i).equalsIgnoreCase(sen.get(j)))
                       flag=1; 
            }
            if(flag==1)
            {
                bw.write(predclass.get(i));
               // System.out.println(predclass.get(i));
                bw.newLine();
                flag=0;
            }
        }
        bw.close();
        fw.close();
      
        fr.close();
        br.close();
    }
    void stopwords() throws IOException
    {
        List<String> class1=new ArrayList<String>();
        List<String> stop=new ArrayList<String>();
        List<String> noun1=new ArrayList<String>();
        String line;
        FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\lemma_class.txt") ;
        BufferedReader br=new BufferedReader(fr);
        
        FileReader fr1 = new FileReader("C:\\Users\\shweta\\Desktop\\test cases\\stopwords2.txt") ;
        BufferedReader br1=new BufferedReader(fr1);
       
        while((line=br.readLine())!= null)
        {
            class1.add(line);
        }
        
         while((line=br1.readLine())!= null)
        {
            stop.add(line);
        }
       
        for(int i=0;i<class1.size();i++)
        {       
            int flag=0;
            for(int j=0;j<stop.size();j++)
            {            
                if(class1.get(i).contains(stop.get(j))) {
                      
                     flag=1;                 
                }
            }
            if(flag==0)
            {
                noun1.add(class1.get(i));
            }
        }
        fr.close();
        br.close();
        
        HashSet<String> dup = new HashSet<String>(noun1);
        List<String> nondup = new ArrayList<String>(dup);
       
        FileWriter fw = new FileWriter("D:\\RESEARCH\\New program files\\class.txt");
        BufferedWriter bw = new BufferedWriter(fw);
    
        for(int i=0;i<nondup.size();i++)
        {
            bw.write(nondup.get(i));
           //System.out.println(nondup.get(i));
            bw.newLine();
        }       
        bw.close();
        fw.close();
        
        fr1.close();
        br1.close();
        
    }
    void passive_subject() throws  IOException
    {
        String line;
        List<String> sub=new ArrayList<String>();
        List<String> sen=new ArrayList<String>();
        List<String> sen1=new ArrayList<String>();
        List<String> sub1=new ArrayList<String>();
        List<String> sub2=new ArrayList<String>();
        FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\compound.txt");
        BufferedReader br=new BufferedReader(fr);
         while((line=br.readLine())!= null)
        {
            sen.add(line);
        } 
         br.close();
         fr.close();
          
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
                if(st[j].contains("nsubjpass(") )            
                {         
                    sub.add(st[j+1].split("\\-")[0].toLowerCase());
                }       
            }
        }
        Properties props = new Properties(); 
        props.put("annotators", "tokenize, ssplit, pos, lemma"); 
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
        for(int i=0;i<sub.size();i++)
        {     
            String full="";
             Annotation document = pipeline.process(sub.get(i).replace("_", ","));  
                for(CoreLabel token: document.get(CoreAnnotations.TokensAnnotation.class))
                {   
                    String word = token.get(CoreAnnotations.TextAnnotation.class);                
                    String lemma = token.get(CoreAnnotations.LemmaAnnotation.class); 
                    full=full+lemma;
                }
                  sub1.add(i, full.replace(",", "_"));
        }
         List<String> remove=new ArrayList<String>();
        for(int k=0;k<sub1.size();k++)
        {
            int freq=0;
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
                   if(st[j].contains("(") )            
                    {  
                       // System.out.println(st[j]);
                        if(st[j].contains("dobj("))
                        {
                            if(st[j].split("\\(")[1].split("-")[0].equalsIgnoreCase(sub1.get(k)))
                            {    
                                remove.add(sub1.get(k));
                                break;
                            }
                        }
                            
                        if(st[j].split("\\(")[1].split("-")[0].equalsIgnoreCase(sub1.get(k)))
                        {
                            freq++;
                        }
                    }
                   
                   if(st[j].contains(")"))
                   {
                       if(st[j].split("-")[0].equalsIgnoreCase(sub1.get(k)))
                       {
                           freq++;
                       }
                   }
               } 
            }
        
           if(freq>1)
           {
               sub2.add(sub1.get(k));
               //System.out.println(sub1.get(k));
           }
        }
        for(int i=0;i<remove.size();i++)
        {
           // System.out.println(remove.get(i));
            for(int j=0;j<sub2.size();j++)
            {
                if(sub2.get(j).equalsIgnoreCase(remove.get(i)))
                {
                    sub2.remove(j);
                    break;
                }
            }
        }
        
        FileWriter fw = new FileWriter("D:\\RESEARCH\\new program files\\class.txt",true) ;
        BufferedWriter bw=new BufferedWriter(fw);
        
        HashSet<String> attdup = new HashSet<String>(sub2);
        List<String> attnondup = new ArrayList<String>(attdup); 

        for(int i=0;i<attnondup.size();i++)
        {
            bw.write(attnondup.get(i));
            //System.out.println(attnondup.get(i));
            bw.newLine();
        }
        
        bw.close();
        fw.close();
        
        br.close();
        fr.close();
    }
    List<String> indef_adj() throws IOException
    {
         FileReader fr = new FileReader("D:\\RESEARCH\\new program files\\compound.txt");
         BufferedReader br=new BufferedReader(fr);

         String line;
         List<String> sen=new ArrayList<String>();
          List<String> sub=new ArrayList<String>();
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
                if(st[j].contains("amod(") )            
                {
                    if(st[j+1].split("-")[0].matches("(?i)any|few|many|much|most|several|some"))
                        sub.add(st[j].split("\\(")[1].split("-")[0]);
                }
                if(st[j].contains("det(") )            
                {
                    if(st[j+1].split("-")[0].equalsIgnoreCase("each"))
                    {
                        if(st[j+2].contains("compound("))
                        {
                            sub.add(st[j+2].split("\\(")[1].split("-")[0]);
                        }
                        else
                        {
                            sub.add(st[j].split("\\(")[1].split("-")[0]);
                        }
                    }
                }
               if(j+3<tokenno)
               {
                   if(st[j].contains("nsubj(")&& st[j+1]!=null && st[j+2].contains("case(") && st[j+3].contains("of-"))            
                        sub.add(st[j+2].split("\\(")[1].split("-")[0]);
               }
                
            }
        }
        List<String> sub2 = new ArrayList<String>();
      
        for(int i=0;i<sub.size();i++)
        {
            int freq=0;
            for(int j=0;j<sen.size();j++)
            {
                    if( sen.get(j).contains(sub.get(i)))
                    {                     
                      freq++;   
                    }
                if(freq>1)
                {
                    sub2.add(sub.get(i));
                    break;
                }  
            }
            
        }

        Properties props = new Properties(); 
        props.put("annotators", "tokenize, ssplit, pos, lemma"); 
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
         for(int i=0;i<sub2.size();i++)
        {  
           //System.out.println(sub2.get(i));
            Annotation document = pipeline.process(sub2.get(i));  
            for(CoreLabel token: document.get(CoreAnnotations.TokensAnnotation.class))
            {   
                 String word = token.get(CoreAnnotations.TextAnnotation.class);                
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);  
                sub2.set(i,lemma);
            }
        }
        HashSet<String> attdup = new HashSet<String>(sub2);
        List<String> attnondup = new ArrayList<String>(attdup);
        br.close();
        fr.close();
        return attnondup;
    }
            
}
