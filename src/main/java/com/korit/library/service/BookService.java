package com.korit.library.service;

import com.korit.library.exception.CustomValidationException;
import com.korit.library.repository.BookRepository;
import com.korit.library.web.dto.*;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Service
public class BookService {

//    yml 경로를 여기로 들고 와라는 코드임
//    표현식
    @Value("${file.path}")
    private String filePath;

    @Autowired
    private BookRepository bookRepository;

    public List<BookMstDto> searchBook(SearchReqDto searchReqDto) {
        searchReqDto.setIndex();
        return bookRepository.searchBook(searchReqDto);
    }

    public List<CategoryDto> getCategories() {
        return bookRepository.findAllCategory();
    }

    public void registerBook(BookReqDto bookReqDto) {
        duplicateBookCode((bookReqDto.getBookCode()));
        bookRepository.saveBook(bookReqDto);
    }

    private void duplicateBookCode(String bookCode) {
       BookMstDto bookMstDto = bookRepository.findBookByBookCode(bookCode);
       if(bookMstDto != null) {
           Map<String, String> errorMap = new HashMap<>();
           errorMap.put("bookCode", "이미 존재하는 도서 코드입니다.");

           throw new CustomValidationException(errorMap);
       }
    }

    public void modifyBook(BookReqDto bookReqDto) {
        bookRepository.updateBookByBookCode(bookReqDto);
    }
    public void maintainModifyBook(BookReqDto bookReqDto) {
        bookRepository.maintainUpdateBookByBookCode(bookReqDto);
    }

    public void removeBook(String bookCode) {
        bookRepository.deleteBook(bookCode);
    }

    public void registerBookImages(String bookCode, List<MultipartFile> files) {
        if(files.size() < 1) {
            Map<String, String> errorMap = new HashMap<String, String>();
            errorMap.put("files", "이미지를 선택하세요.");

            throw new CustomValidationException(errorMap);
        }

        List<BookImageDto> bookImageDtos = new ArrayList<BookImageDto>();

        files.forEach(file -> {
            String originFileName = file.getOriginalFilename();
            String extension = originFileName.substring(originFileName.lastIndexOf("."));
            String tempFileName = UUID.randomUUID().toString().replaceAll("-", "") + extension;

            Path uploadPath = Paths.get(filePath + "book/" + tempFileName);

            File f = new File(filePath + "book");
//            경로가 없으면 true
            if(!f.exists()) {
//          mkdirs 파일 입출력 때 쓰는 거 경로가 없으면 모든 경로를 생성해라.
                f.mkdirs();
            }

            try {
                Files.write(uploadPath, file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            BookImageDto bookImageDto = BookImageDto.builder()
                    .bookCode(bookCode)
                    .saveName(tempFileName)
                    .originName(originFileName)
                    .build();

            bookImageDtos.add(bookImageDto);
        });

        bookRepository.registerBookImages(bookImageDtos);

    }

    public List<BookImageDto> getBooks(String bookCode) {
        return bookRepository.findBookImageAll(bookCode);
    }

    public void removeBookImage(int imageId) {
        BookImageDto bookImageDto = bookRepository.findBookImageByImageId(imageId);

        if(bookImageDto == null) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "존재하지 않는 이미지 ID입니다.");

            throw new CustomValidationException(errorMap);
        }

        if(bookRepository.deleteBookImages(imageId) > 0) {
            File file = new File(filePath + "book/" + bookImageDto.getSaveName());
            if(file.exists()) {
                file.delete();
            }


        }
    }

}
