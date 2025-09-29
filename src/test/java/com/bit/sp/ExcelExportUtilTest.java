package com.bit.sp;

import com.bit.sp.dto.UserDto;
import com.bit.sp.utils.ExcelExportUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcelExportUtilTest {
    private ExcelExportUtil excelExportUtil;
    private HttpServletResponse response;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() throws IOException {
        excelExportUtil = new ExcelExportUtil();
        response = mock(HttpServletResponse.class);
        outputStream = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new jakarta.servlet.ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }
            @Override
            public boolean isReady() { return true; }
            @Override
            public void setWriteListener(jakarta.servlet.WriteListener writeListener) {}
        });
    }

    @Test
    void testExportToExcel_withValidList() throws IOException {
        List<UserDto> users = Arrays.asList(
                UserDto.builder().id(1L).username("user1").status("active").email("a@b.com").build(),
                UserDto.builder().id(2L).username("user2").status("inactive").email("c@d.com").build()
        );
        excelExportUtil.exportToExcel(response, users, "TestReport");
        assertTrue(outputStream.size() > 0, "Excel file should be written to output stream");
        assertDoesNotThrow(() -> WorkbookFactory.create(new java.io.ByteArrayInputStream(outputStream.toByteArray())));
    }

    @Test
    void testExportToExcel_withEmptyList_throwsException() {
        List<UserDto> users = Collections.emptyList();
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                excelExportUtil.exportToExcel(response, users, "TestReport")
        );
        assertEquals("Data list cannot be null or empty", ex.getMessage());
    }

    @Test
    void testExportToExcel_withNullList_throwsException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                excelExportUtil.exportToExcel(response, null, "TestReport")
        );
        assertEquals("Data list cannot be null or empty", ex.getMessage());
    }

    @Test
    void testExportToExcel_genericMethod_withValidList() throws IOException {
        List<UserDto> users = Arrays.asList(
                UserDto.builder().id(1L).username("user1").status("active").email("a@b.com").build()
        );
        List<String> headers = Arrays.asList("ID", "Username", "Status", "Email");
        List<String> fields = Arrays.asList("id", "username", "status", "email");
        excelExportUtil.exportToExcel(response, users, headers, fields, "TestReport");
        assertTrue(outputStream.size() > 0);
        assertDoesNotThrow(() -> WorkbookFactory.create(new java.io.ByteArrayInputStream(outputStream.toByteArray())));
    }

    @Test
    void testExportToExcel_genericMethod_withEmptyList_throwsException() {
        List<UserDto> users = Collections.emptyList();
        List<String> headers = Arrays.asList("ID", "Username", "Status", "Email");
        List<String> fields = Arrays.asList("id", "username", "status", "email");
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                excelExportUtil.exportToExcel(response, users, headers, fields, "TestReport")
        );
        assertEquals("Data list cannot be null or empty", ex.getMessage());
    }
}
