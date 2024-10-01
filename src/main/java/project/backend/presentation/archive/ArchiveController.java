package project.backend.presentation.archive;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.business.archive.ArchiveService;
import project.backend.business.archive.respone.CreateUpdateArchiveResponse;
import project.backend.presentation.archive.request.CreateUpdateArchiveRequest;
import project.backend.security.aop.AssignCurrentUserInfo;
import project.backend.security.aop.CurrentUserInfo;

@RestController
@RequestMapping("/archives")
@RequiredArgsConstructor
public class ArchiveController {

  private final ArchiveService archiveService;

  @AssignCurrentUserInfo
  @PostMapping
  public ResponseEntity<CreateUpdateArchiveResponse> createArchive(CurrentUserInfo userInfo,
      @Valid @RequestBody CreateUpdateArchiveRequest archiveRequest) {
    CreateUpdateArchiveResponse response = archiveService.createArchive(
        userInfo.getUserId(), archiveRequest.toServiceRequest());
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}
