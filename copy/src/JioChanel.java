import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.io.IOException;

public class JioChanel {
    public static void main(String[] args) {
        JioChanel chanel = new JioChanel();
        try {
            if(args.length<3){
                System.out.println("usage: Jiochanel <source> <destination> <mode>\n");
                System.out.println(args.length);
                return;
            }
            if("1".equals(args[2])){
                long start = System.currentTimeMillis();
                chanel.copy(args[0],args[1]);
                long end = System.currentTimeMillis();
                long time = end-start;
                System.out.println("Time copy "+ time +"millisecond");
                System.out.println(args[0]);
                System.out.println(args[1]);
                System.out.println(args[2]);
                System.out.println(args[3]);
            }else{
                long start = System.currentTimeMillis();
                chanel.zeroCopy(args[0],args[1]);
                long end = System.currentTimeMillis();
                long time = end-start;
                System.out.println("Time zerocopy "+ time +"millisecond");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void copy(String from,String to) throws Exception{
        byte[] data = new byte[8*1024];
        FileInputStream fis = null;
        FileOutputStream fos = null;
        long byteToCopy = new File(from).length();
        long byteCopied =0;
        try{
            fis = new FileInputStream(from);
            fos = new FileOutputStream(to);
            
            while(byteCopied<byteToCopy){
                fis.read(data);
                fos.write(data);
                byteCopied += data.length;
            }
            fos.flush();
        }finally{
            if(fis != null){
                fis.close();
            }
            if(fos != null){
                fos.close();
            }
        }
    }
    public void zeroCopy(String from,String to) throws IOException{
        FileChannel source = null;
        FileChannel destination = null;
        try{
            source = new FileInputStream(from).getChannel();
            destination = new FileOutputStream(to).getChannel();
            source.transferTo(0, source.size(), destination);
        }finally{
            if(source != null){
                source.close();
            }
            if(destination !=null){
                destination.close();
            }

        }
    }
}
