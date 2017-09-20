
package unstruct;
import java.util.*;
import edu.stanford.nlp.process.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp. pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.io.*;
public class Unstruct {

    public static void main(String[] args) throws IOException {
        
        String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
        LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
        String  textFile="C:\\Users\\shweta\\Desktop\\test cases\\course registration.txt";
        demoDP(lp, textFile); 

     //calling method for seprating the sentences and store it into the arraylist SEN
        fileread fread=new fileread();
        List<String> sen = new ArrayList<String>(); 

        sen=fread.file_read();

        //converting sen arraylist into array SENTENCE
        String []sentence = new String[sen.size()];
        sen.toArray(sentence);
        int totalsen =sen.size();

        //calling method for storing noun in the NOUN
        sen = fread.noun_phrase(sentence,totalsen);

        //converting the arraylist into NOUN array for storing noun
        String noun[]=new String[sen.size()];
        sen.toArray(noun);
        int totalnoun =sen.size();
       
        fread.compound();
        fread.subject();        
       
        fread.predicate_noun();
        fread.passive_subject();
        extract ext=new extract();
        ext.extract_attributes();
        ext.identified_attributes();
        
        ext.enter_attributes();
        ext.acc_attributes();
        ext.their_attributes();
        ext.foron_including();
        ext.poss_verb_attributes();
        lemma("D:\\RESEARCH\\new program files\\class.txt");
        fread.stopwords();
        rel_extract oprel=new rel_extract();
        oprel.op_extract();
        oprel.generalization();
        spearate sep=new spearate();
        sep.accumulate_attribue();
        sep.sparate_class();

    }
    

    public static void lemma(String filename) throws IOException
    {
        String line,text = null;
        Properties props = new Properties(); 
        props.put("annotators", "tokenize, ssplit, pos, lemma"); 
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
        FileReader fr = new FileReader(filename);
        BufferedReader br=new BufferedReader(fr);
        FileWriter fw= new FileWriter("D:\\RESEARCH\\new program files\\lemma_class.txt");
        BufferedWriter bw=new BufferedWriter(fw);
        while((line=br.readLine())!= null)
        {
            int f=0;
            Annotation document = pipeline.process(line.replace("_", "-"));  
            for(CoreLabel token: document.get(CoreAnnotations.TokensAnnotation.class))
            {   
                String word = token.get(CoreAnnotations.TextAnnotation.class);                
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class); 
                bw.write(lemma.replace("-","_"));  
            }
            bw.newLine();
        }
         
        bw.close();
        fw.close();
        fr.close();
        br.close();
    }
    public static void demoDP(LexicalizedParser lp, String filename) throws IOException 
    {
   
        FileWriter fw= new FileWriter("D:\\RESEARCH\\new program files\\parsed.txt");
        BufferedWriter bw=new BufferedWriter(fw);
        FileWriter fw1= new FileWriter("D:\\RESEARCH\\new program files\\dependency.txt");
        BufferedWriter bw1=new BufferedWriter(fw1);
        
        TreebankLanguagePack tlp = lp.treebankLanguagePack(); 
        GrammaticalStructureFactory gsf = null;
        
        if (tlp.supportsGrammaticalStructures()) {
            gsf = tlp.grammaticalStructureFactory();
        }
   
        for (List<HasWord> sentence : new DocumentPreprocessor(filename)) {
            Tree parse = lp.apply(sentence);            
            String st=parse.pennString();
            bw.write(st);           
            
            if (gsf != null) {
                GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
                List<TypedDependency>  tdl = gs.typedDependenciesCCprocessed();
                String dep=tdl.toString();
                bw1.write(dep);
                bw1.newLine();
                 
            }
        }
        bw.close();
        fw.close();
        bw1.close();
        fw1.close();
    }
    
    
}
    

