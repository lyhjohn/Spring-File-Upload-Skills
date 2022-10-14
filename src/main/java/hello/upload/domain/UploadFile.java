package hello.upload.domain;

import lombok.Data;

import java.util.Map;

@Data
public class UploadFile {

    // 여러 사람이 동일한 파일 이름으로 업로드할 수 있으므로 storeFileName 으로 구분짓기
    private String uploadFileName;
    private String storeFileName;


    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
