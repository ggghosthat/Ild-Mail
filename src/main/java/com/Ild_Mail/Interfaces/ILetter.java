package com.Ild_Mail.Interfaces;

import java.io.File;
import java.util.List;

public interface ILetter {

    String getSubject();
    void setSubject(String subject);

    String getContent();
    void setContent(String content);

    List<File> getFiles();
    void setFiles(List<File> files);
    void AddFile(File file);
    void ClearFileStruction();
    void RemoveFile(int index);
    void InsertFile(int index, File file) throws Exception;
    void ModifyFile(int index, File file);
}
