package cn.ymotel.dpress.admin.attachments;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.sequence.IdWorker;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.utils.FilenameUtils;
import run.halo.app.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ActorCfg(urlPatterns = "/api/admin/attachments/upload",methods = RequestMethod.POST)
public class UploadActor  implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;

    @Autowired
    OptionsService optionsService;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
       byte[] bs= message.getFileBytes("file");
        String originalBasename = FilenameUtils.getBasename(Objects.requireNonNull( message.getFile("file").getOriginalFilename()));
        String extension = FilenameUtils.getExtension(message.getFile("file").getOriginalFilename());
        Map params=new HashMap<>();
        params.put("create_time",new java.sql.Timestamp(System.currentTimeMillis()));
        params.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        params.put("siteid", Utils.getSiteId());
        params.put("path", "upload/"+IdWorker.getInstance().nextId()+"."+extension);
        params.put("thumb_path",params.get("path"));
        params.put("file_key",params.get("path"));
        params.put("content",bs);
        params.put("size",bs.length);
        params.put("suffix",extension);
        params.put("name",originalBasename);
        params.put("media_type",message.getFile("file").getContentType());
        String type=optionsService.getOption(Utils.getSiteId(),"attachment_type","LOCAL");
        if(type.equals("LOCAL")) {
            params.put("type","0" );
        }
        params.putAll(generateProperty(bs,extension,MediaType.valueOf(Objects.requireNonNull(message.getFile("file").getContentType()))));
        sqlSession.insert("attachmentfiles.i",params);
        System.out.println("upload---");
        return params;
    }
    MediaType IMAGE_TYPE = MediaType.valueOf("image/*");

    public Map generateProperty(byte[] bs, String extension, MediaType mediaType) throws IOException {
        boolean isSvg = "svg".equals(extension);
        if(isSvg){
            return new HashMap();
        }
        if(!IMAGE_TYPE.includes(mediaType)){
            return new HashMap();
        }
        Map map=new HashMap();
        BufferedImage originalImage = ImageUtils.getImageFromFile(new ByteArrayInputStream(bs), extension);
        map.put("height",originalImage.getHeight());
        map.put("width",originalImage.getWidth());
        return map;

    }
}
