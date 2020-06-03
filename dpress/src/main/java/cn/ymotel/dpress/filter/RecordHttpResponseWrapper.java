package cn.ymotel.dpress.filter;

import com.alicp.jetcache.Cache;
import org.springframework.lang.Nullable;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

public class RecordHttpResponseWrapper extends HttpServletResponseWrapper {
   private FastByteArrayOutputStream content = new FastByteArrayOutputStream(1024);
   private CacheResponseEntity cacheResponseEntity=null;
    @Nullable
    private ServletOutputStream outputStream;

    @Nullable
    private PrintWriter writer;

    private  String path;
    private Cache cache=null;

    public void putCache(){
        cacheResponseEntity.setContent(content.toByteArray());
        cache.put(path,cacheResponseEntity);
    }

    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response the {@link HttpServletResponse} to be wrapped.
     * @throws IllegalArgumentException if the response is null
     */
    public RecordHttpResponseWrapper(HttpServletResponse response,String path,Cache cache) {
        super(response);
        this.cacheResponseEntity=new CacheResponseEntity();
        this.path=path;
        this.cache=cache;
    }

    @Override
    public void flushBuffer() throws IOException {
        super.flushBuffer();
    }

    @Override
    public void setCharacterEncoding(String charset) {
        super.setCharacterEncoding(charset);
        this.cacheResponseEntity.setEncoding(charset);
    }

    @Override
    public void setContentType(String type) {
        super.setContentType(type);
        this.cacheResponseEntity.setContentType(type);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (this.outputStream == null) {
            this.outputStream = new ResponseServletOutputStream(getResponse().getOutputStream());
        }
        return this.outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if(writer==null){
            String characterEncoding = getCharacterEncoding();
            if(characterEncoding==null){
                characterEncoding=WebUtils.DEFAULT_CHARACTER_ENCODING;
            }
            this.writer=new PrintWriter(new OutputStreamWriter(getOutputStream(), characterEncoding));

        }
        return writer;
    }
    private class ResponseServletOutputStream extends ServletOutputStream {

        private final ServletOutputStream os;

        public ResponseServletOutputStream(ServletOutputStream os) {
            this.os = os;


        }



        @Override
        public void write(int b) throws IOException {
            os.write(b);
             content.write(b);
        }

        @Override
        public void flush() throws IOException {
            super.flush();
            content.flush();
        }

//        @Override
//        public void write(byte[] b, int off, int len) throws IOException {
//            super.write(b,off,len);
//            content.write(b, off, len);
//        }

//        @Override
//        public void close() throws IOException {
//            super.close();
//            cache.put(path,cacheResponseEntity);
//        }

        @Override
        public boolean isReady() {
            return this.os.isReady();
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            this.os.setWriteListener(writeListener);
        }
    }



}
