package com.blsa.ezilog.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blsa.ezilog.dao.SelectionHistoryDao;
import com.blsa.ezilog.dao.SelectionPostDao;
import com.blsa.ezilog.model.post.SelectionHistory;
import com.blsa.ezilog.model.post.SelectionPost;
import com.blsa.ezilog.model.post.SelectionPostRequestDTO;

@Service
public class SelectionService{

    @Autowired
    SelectionPostDao selectionPostDao;
    
    @Autowired
    SelectionHistoryDao selectionHistoryDao;
    
    @Transactional
    public SelectionHistory removeMainPostandAddtoHistory(SelectionPostRequestDTO post) {
        Optional<SelectionPost> selectionPostOptional = selectionPostDao.findByNo(post.getNo());
        
        if(selectionPostOptional.isPresent()) {
            SelectionHistory selectionHistory = new SelectionHistory();
            selectionHistory.setNo(post.getNo());
            selectionHistory.setDescription(post.getDescription());
            
            selectionHistoryDao.save(selectionHistory);
            selectionPostDao.deleteByNo(post.getNo());
            
            return selectionHistory;
        }else {
            return null;
        }
        
    }

}
