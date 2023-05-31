//package org.mint.smallcloud.file.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.mint.smallcloud.exception.ExceptionStatus;
//import org.mint.smallcloud.exception.ServiceException;
//import org.mint.smallcloud.file.domain.DataNode;
//import org.mint.smallcloud.file.repository.DataNodeRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class DataNodeThrowerService {
//    private final DataNodeRepository dataNodeRepository;
//
//    public List<DataNode> findByName(String labelName) {
//        List<DataNode> dataNode;
//        try {
//            dataNode = dataNodeRepository.findByName(labelName);
//        } catch (Exception e) {
//            throw new ServiceException(ExceptionStatus.FILE_NOT_FOUND);
//        }
//            return dataNode;
//    }
//}
