package com.develonity.board.service;

import com.develonity.board.entity.Scrap;
import com.develonity.board.repository.ScrapRepository;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService {

  private final ScrapRepository scrapRepository;

  // 스크랩 추가
  @Transactional
  @Override
  public void addScrap(Long userId, Long boardId) {
    if (existsScrapBoardIdAndUserId(boardId, userId)) {
      throw new CustomException(ExceptionStatus.SCRAP_IS_EXIST);
    }
    Scrap scrap = new Scrap(userId, boardId);
    scrapRepository.save(scrap);

  }


  // 스크랩 취소
  @Transactional
  @Override
  public void cancelScrap(Long userId, Long boardId) {
    if (!existsScrapBoardIdAndUserId(boardId, userId)) {
      throw new CustomException(ExceptionStatus.SCRAP_IS_NOT_EXIST);
    }
    Scrap scrap = scrapRepository.findByBoardIdAndUserId(boardId, userId);
    scrapRepository.delete(scrap);
  }


  //내가 스크랩 게시물 조회
  @Transactional
  @Override
  public List<Long> getScrapBoardIds(Long userId) {
    List<Scrap> scraps = scrapRepository.findAllByUserId(userId);
    List<Long> scrapBoardIds = new ArrayList<>();
    for (Scrap scrap : scraps) {
      Long scrapBoardId = scrap.getBoardId();
      scrapBoardIds.add(scrapBoardId);
    }
    return scrapBoardIds;
  }


  //유저가 총 스크랩한 갯수
  @Override
  public long countScraps(Long userId) {
    return scrapRepository.countByUserId(userId);
  }

  //게시글 삭제시 해당 보드 관련 스크랩도 전부 삭제
  @Override
  public void deleteScraps(Long boardId) {
    scrapRepository.deleteAllByBoardId(boardId);
  }

  //해당 유저가 해당 게시글에 스크랩 했는지 여부
  @Override
  public boolean existsScrapBoardIdAndUserId(Long boardId, Long userId) {
    return scrapRepository.existsScrapByBoardIdAndUserId(boardId, userId);
  }

}

