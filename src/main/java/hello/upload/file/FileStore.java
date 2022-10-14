package hello.upload.file;

import hello.upload.domain.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;
    
    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        // 실제 파일명 / 경로 없이 저장
        String originalFilename = multipartFile.getOriginalFilename();

        // 서버에 저장하는 파일명(UUID + 확장자) / 경로 없이 저장
        String storeFileName = createStoreFileName(originalFilename);

        // 경로 + 파일명으로 실제 경로에 파일 복사해서 생성
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(originalFilename, storeFileName);
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        if (originalFilename == null) {
            return null;
        }

        // 확장자 추출
        String ext = extractExt(originalFilename);

        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int extPos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(extPos + 1);
    }
}
