package com.develonity.board.repository;

import com.develonity.board.entity.CommunityBoard;
import com.develonity.board.entity.CommunityCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityBoardRepository extends JpaRepository<CommunityBoard, Long>,
    CommunityBoardRepositoryCustom {

  boolean existsBoardById(Long id);

  Page<CommunityBoard> findByCommunityCategoryAndTitleContainingOrContentContaining(
      CommunityCategory communityCategory, String title, String content,
      Pageable pageable);

  boolean existsCommunityBoardByIdAndCommunityCategory(Long id,
      CommunityCategory communityCategory);

  //테스트용
  Page<CommunityBoard> findByCommunityCategory(
      CommunityCategory communityCategory,
      Pageable pageable);
}
