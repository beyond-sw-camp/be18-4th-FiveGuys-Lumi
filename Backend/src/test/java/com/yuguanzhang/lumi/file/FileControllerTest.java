package com.yuguanzhang.lumi.file;

import com.yuguanzhang.lumi.file.controller.FileController;
import com.yuguanzhang.lumi.file.dto.FileDownloadDto;
import com.yuguanzhang.lumi.file.dto.FileUploadResponseDto;
import com.yuguanzhang.lumi.file.enums.EntityType;
import com.yuguanzhang.lumi.file.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FileControllerTest {
    private MockMvc mockMvc;
    private FileService fileService;

    @BeforeEach
    void setup() {
        // FileService를 Mockito로 모킹
        fileService = Mockito.mock(FileService.class);

        // FileController만 standalone 설정
        mockMvc = MockMvcBuilders.standaloneSetup(new FileController(fileService)).build();
    }

    @Test
    @DisplayName("파일 업로드 성공")
    void testUploadFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello".getBytes());

        // DTO 구조에 맞게 생성
        FileUploadResponseDto dto = new FileUploadResponseDto(1L, "test.txt");

        Mockito.when(fileService.uploadFile(any(EntityType.class), any(MockMultipartFile.class)))
                .thenReturn(dto);

        mockMvc.perform(multipart("/api/ASSIGNMENT/files/upload")
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data[0].fileId").value(1L))
                .andExpect(jsonPath("$.data[0].fileName").value("test.txt"));
    }

    @Test
    @DisplayName("파일 다운로드 성공")
    void testDownloadFile() throws Exception {
        ByteArrayResource resource = new ByteArrayResource("Hello".getBytes());
        FileDownloadDto dto = new FileDownloadDto(resource, "test.txt", resource.contentLength(), "application/pdf");

        Mockito.when(fileService.downloadFile(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/api/files/1/download"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        org.hamcrest.Matchers.containsString("test.txt")))
                .andExpect(content().contentType("application/pdf"));
    }

    @Test
    @DisplayName("파일 삭제 성공")
    void testDeleteFile() throws Exception {
        doNothing().when(fileService).deleteFile(anyLong());

        mockMvc.perform(delete("/api/files/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}