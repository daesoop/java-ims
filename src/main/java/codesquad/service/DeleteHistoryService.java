package codesquad.service;

import codesquad.domain.DeleteHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DeleteHistoryService {

    @Resource(name = "deleteHistoryRepository")
    private DeleteHistoryRepository deleteHistoryRepository;

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void saveAll() {
//        deleteHistoryRepository.save();
//    }
}
