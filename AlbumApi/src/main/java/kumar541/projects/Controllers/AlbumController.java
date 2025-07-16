package kumar541.projects.Controllers;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.random.RandomGenerator;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kumar541.projects.Modal.Account;
import kumar541.projects.Modal.Album;
import kumar541.projects.Modal.Photo;
import kumar541.projects.Services.AccountService;
import kumar541.projects.Services.AlbumService;
import kumar541.projects.Services.PhotoService;
import kumar541.projects.payload.Album.AlbumPayloadDTO;
import kumar541.projects.payload.Album.AlbumViewDTO;
import kumar541.projects.payload.Album.PhotoDTO;
import kumar541.projects.payload.Album.PhotoPayloadDTO;
import kumar541.projects.payload.Album.PhotoViewDTO;
import kumar541.projects.utils.AppUtils.AppUtil;
import kumar541.projects.utils.constatnts.AlbumError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1")
@Tag(name="Album Controller",description="Album and Photo managment")
@Slf4j
@CrossOrigin(origins="http://localhost:3000",maxAge = 3600)
public class AlbumController {

    private final String PHOTO_FOLDER_NAME="photos";
    private final String THUMBNAIL_FOLDER_NAME="thumbnails";
    private final int THUMBNAIL_WIDTH=300;

    @Autowired 
    private AccountService accountService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private PhotoService photoService;



    @PostMapping(value="/albums/add",consumes = "application/json",produces = "application/json" )
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary="Add a new Album")
    @ApiResponse(responseCode="200",description = "Album Created")
    @ApiResponse(responseCode="401",description = "Token Missing")
    @ApiResponse(responseCode="403",description = "Token Error")
    @SecurityRequirement(name="Album Api")
    public ResponseEntity<AlbumViewDTO> addAlbum(@Valid @RequestBody AlbumPayloadDTO albumPayloadDTO, Authentication authentication){
        
        try {
            
            Album album= new Album();
            
            album.setName(albumPayloadDTO.getName());
            album.setDescription(albumPayloadDTO.getDescription());

            String email= authentication.getName();
            Optional<Account> optionalAccount=  accountService.findByEmail(email);
            Account acc= optionalAccount.get();
            album.setAccount(acc);
            album= albumService.save(album);
            return ResponseEntity.ok(new AlbumViewDTO(album.getId(),album.getName(),album.getDescription(),null));

        } catch (Exception e) {
            log.debug(AlbumError.ADD_ALBUM_ERROR.toString() + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @GetMapping(value="/albums",produces = "application/json" )
    @Operation(summary="List of Albums")
    @ApiResponse(responseCode="200",description = "List of Albums")
    @ApiResponse(responseCode="401",description = "Token Missing")
    @ApiResponse(responseCode="403",description = "Token Error")
    @SecurityRequirement(name="Album Api")
    public List<AlbumViewDTO> albums(Authentication authentication){
        
        String email= authentication.getName();
        Optional<Account> optionalAccount=  accountService.findByEmail(email);
        Account acc= optionalAccount.get();

        List<AlbumViewDTO> list= new ArrayList<>();

        for(Album album : albumService.findByAccount_id(acc.getId())){

            List<PhotoDTO> photoList= new ArrayList<>();

            for(Photo photo1 : photoService.findByAlbum_id(album.getId())){

                String link="/albums/"+album.getId()+"/photos/"+photo1.getId()+"/download-photo";

                PhotoDTO photoDTO= new PhotoDTO(photo1.getId(),photo1.getName(),photo1.getDescription(),photo1.getOriginalFileName(),link);
                photoList.add(photoDTO);

            }

            AlbumViewDTO albumViewDTO= new AlbumViewDTO(album.getId(),album.getName(),album.getDescription(),photoList);
            list.add(albumViewDTO);

        }
        return list;
    }

    @PutMapping(value = "/albums/{album_id}/update", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please add valid name and description")
    @ApiResponse(responseCode = "204", description = "Album updated")
    @Operation(summary = "Update an Album")
    @SecurityRequirement(name="Album Api")
    public ResponseEntity<AlbumViewDTO> update_Album(
            @Valid @RequestBody AlbumPayloadDTO albumPayloadDTO,
            @PathVariable long album_id,
            Authentication authentication) {

        try {
            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            Account account = optionalAccount.get();

            Optional<Album> optionalAlbum = albumService.findById(album_id);
            Album album;

            if (optionalAlbum.isPresent()) {
                album = optionalAlbum.get();

                if (account.getId() != album.getAccount().getId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }

                album.setName(albumPayloadDTO.getName());
                album.setDescription(albumPayloadDTO.getDescription());
                album = albumService.save(album);

                List<PhotoDTO> photos = new ArrayList<>();
                for (Photo photo : photoService.findByAlbum_id(album.getId())) {
                    String link = "/albums/" + album.getId() + "/photos/" + photo.getId() + "/download-photo";
                    photos.add(new PhotoDTO(photo.getId(), photo.getName(), photo.getDescription(),
                            photo.getFileName(), link));
                }

                AlbumViewDTO albumViewDTO = new AlbumViewDTO(
                        album.getId(),
                        album.getName(),
                        album.getDescription(),
                        photos
                );

                return ResponseEntity.ok(albumViewDTO);

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (Exception e) {
            log.debug(AlbumError.ADD_ALBUM_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping(value = "/albums/{album_id}/photos/{photo_id}/update", consumes = "application/json", produces = "application/json")
    @ApiResponse(responseCode = "400", description = "Please add valid name and description")
    @ApiResponse(responseCode = "204", description = "Album update")
    @Operation(summary = "Update a photo")
    @SecurityRequirement(name="Album Api")
    public ResponseEntity<PhotoViewDTO> update_photo(
            @Valid @RequestBody PhotoPayloadDTO photoPayloadDTO,
            @PathVariable long album_id,
            @PathVariable long photo_id,
            Authentication authentication) {

        try {
            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            Account account = optionalAccount.get();

            Optional<Album> optionalAlbum = albumService.findById(album_id);
            Album album;

            if (optionalAlbum.isPresent()) {
                album = optionalAlbum.get();

                if (account.getId() != album.getAccount().getId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            Optional<Photo> optionalPhoto = photoService.findById(photo_id);

            if (optionalPhoto.isPresent()) {
                Photo photo = optionalPhoto.get();

                if(photo.getAlbum().getId() !=album.getId()){
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }

                photo.setName(photoPayloadDTO.getName());
                photo.setDescription(photoPayloadDTO.getDescription());
                photoService.save(photo);

                PhotoViewDTO photoViewDTO = new PhotoViewDTO(
                    photo.getId(),
                    photoPayloadDTO.getName(),
                    photoPayloadDTO.getDescription()
                );

                return ResponseEntity.ok(photoViewDTO);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (Exception e) {
            log.debug(AlbumError.PHOTO_UPDATE_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }



    @GetMapping(value="/albums/{album_id}",produces = "application/json" )
    @Operation(summary="Find Album by id")
    @ApiResponse(responseCode="200",description = "Album")
    @ApiResponse(responseCode="401",description = "Token Missing")
    @ApiResponse(responseCode="403",description = "Token Error")
    @SecurityRequirement(name="Album Api")
    public ResponseEntity<AlbumViewDTO> album_by_id(@PathVariable long album_id,Authentication authentication){
        
        String email= authentication.getName();
        Optional<Account> optionalAccount=  accountService.findByEmail(email);
        Account acc= optionalAccount.get();
        Optional<Album> albumOptional= albumService.findById(album_id);

        if(!albumOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Album album= albumOptional.get();

        if(acc.getId()!= album.getAccount().getId()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

            List<PhotoDTO> photoList= new ArrayList<>();

            for(Photo photo1 : photoService.findByAlbum_id(album.getId())){

                String link="/albums/"+album.getId()+"/photos/"+photo1.getId()+"/download-photo";

                PhotoDTO photoDTO= new PhotoDTO(photo1.getId(),photo1.getName(),photo1.getDescription(),photo1.getOriginalFileName(),link);
                photoList.add(photoDTO);
        }
         AlbumViewDTO albumViewDTO= new AlbumViewDTO(album.getId(),album.getName(),album.getDescription(),photoList);
        return ResponseEntity.ok(albumViewDTO);
    }

    @PostMapping(value="/albums/{album_id}/upload-photos", consumes = "multipart/form-data")
    @Operation(summary="Upload photos to Album")
    @ApiResponse(responseCode="401",description = "Token Missing")
    @ApiResponse(responseCode="403",description = "Token Error")
    @SecurityRequirement(name="Album Api")
    public ResponseEntity< List <HashMap<String, List<?>>>> photos(@RequestPart(required = true) MultipartFile[] files,@PathVariable Long album_id,Authentication authentication) {
        
        String email= authentication.getName();
        Optional<Account> optionalAccount=  accountService.findByEmail(email);
        Account acc= optionalAccount.get();
        Optional<Album> optionalAlbum= albumService.findById(album_id);

       if(!optionalAlbum.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }

        Album album= optionalAlbum.get();
        if(optionalAlbum.isPresent()){

            if(acc.getId()!= album.getAccount().getId()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        }

        List<PhotoViewDTO> fileNameWithSuccess= new ArrayList<>();
        List<String> fileNameWithError= new ArrayList<>();

        Arrays.asList(files).stream().forEach(file->{
           
            String contentType= file.getContentType();

            if(contentType.equals("image/png")
                || contentType.equals("image/jpeg")
                || contentType.equals("image/jpg")
            ){

                int length=10;
                boolean useLetters=true;
                boolean useNumbers=true;


                try {
                    String fileName= file.getOriginalFilename();
                    String generatedName= RandomStringUtils.random(length,useLetters,useNumbers);
                    String finalName= generatedName+fileName;
                    String absolute_fileLocation= AppUtil.get_photo_upload_path(finalName,PHOTO_FOLDER_NAME,album_id);
                    Path path= Paths.get(absolute_fileLocation);
                    Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
                    Photo photo= new Photo();
                    photo.setName(fileName);
                    photo.setOriginalFileName(fileName);
                    photo.setFileName(finalName);
                    photo.setAlbum(album);
                    photoService.save(photo);

                    PhotoViewDTO photoViewDTO= new PhotoViewDTO(photo.getId(),photo.getName(),photo.getDescription());
                    fileNameWithSuccess.add(photoViewDTO);

                    BufferedImage thumbImg= AppUtil.getThumbnail(file,THUMBNAIL_WIDTH);
                    File thumbnail_location= new File(AppUtil.get_photo_upload_path(finalName,THUMBNAIL_FOLDER_NAME,album_id));
                    ImageIO.write(thumbImg,file.getContentType().split("/")[1],thumbnail_location);

                } catch (Exception e) {
                     log.debug(AlbumError.PHOTO_UPLOAD_ERROR + " " + e.getMessage());
                     fileNameWithError.add(file.getOriginalFilename());
                }

            }

            else{
                fileNameWithError.add(file.getOriginalFilename());
            }
        });

        HashMap<String, List<?>> result = new HashMap<>();
        result.put("SUCCESS", fileNameWithSuccess);
        result.put("ERRORS", fileNameWithError);
        List<HashMap<String, List<?>>> response = new ArrayList<>();
        response.add(result);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/albums/{album_id}/delete")
    @ApiResponse(responseCode = "202", description = "Album deleted")
    @Operation(summary = "Delete an album")
    @SecurityRequirement(name="Album Api")
    public ResponseEntity<String> delete_album(@PathVariable long album_id, Authentication authentication) {
        try {
            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            Account account = optionalAccount.get();

            Optional<Album> optionalAlbum = albumService.findById(album_id);

            if (optionalAlbum.isPresent()) {
                Album album = optionalAlbum.get();

                if (account.getId() != album.getAccount().getId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }

                // Delete all associated photos
                for (Photo photo : photoService.findByAlbum_id(album.getId())) {
                    AppUtil.delete_photo_from_path(photo.getFileName(), PHOTO_FOLDER_NAME, album.getId());
                    AppUtil.delete_photo_from_path(photo.getFileName(), THUMBNAIL_FOLDER_NAME, album.getId());
                    photoService.delete(photo);
                }

                // Delete the album itself
                albumService.deleteAlbum(album);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @DeleteMapping(value = "/albums/{album_id}/photos/{photo_id}/delete")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "202", description = "Photo delete")
    @Operation(summary = "Delete a photo")
    @SecurityRequirement(name="Album Api")
    public ResponseEntity<String> delete_photo(@PathVariable long album_id,
                                            @PathVariable long photo_id,
                                            Authentication authentication) {
        try {
            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            Account account = optionalAccount.get();

            Optional<Album> optionalAlbum = albumService.findById(album_id);
            Album album;

            if (optionalAlbum.isPresent()) {
                album = optionalAlbum.get();

                if (account.getId() != album.getAccount().getId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            Optional<Photo> optionalPhoto = photoService.findById(photo_id);

            if (optionalPhoto.isPresent()) {
                Photo photo = optionalPhoto.get();

                if (photo.getAlbum().getId() != album_id) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }

                AppUtil.delete_photo_from_path(photo.getFileName(), PHOTO_FOLDER_NAME, album_id);
                AppUtil.delete_photo_from_path(photo.getFileName(), THUMBNAIL_FOLDER_NAME, album_id);
                photoService.delete(photo);

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (Exception e) {
            log.debug(AlbumError.PHOTO_DELETE_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @GetMapping("/albums/{album_id}/photos/{photo_id}/download-photo")
    @ApiResponse(responseCode="401",description = "Token Missing")
    @ApiResponse(responseCode="403",description = "Token Error")
    @SecurityRequirement(name="Album Api")
     public ResponseEntity<?> downloadPhoto(@PathVariable("album_id") long album_id,@PathVariable("photo_id") long photo_id,Authentication authentication){

        return downloadFile(album_id, photo_id,PHOTO_FOLDER_NAME, authentication);
     }

    @GetMapping("/albums/{album_id}/photos/{photo_id}/download-thumbnail")
    @ApiResponse(responseCode="401",description = "Token Missing")
    @ApiResponse(responseCode="403",description = "Token Error")
    @SecurityRequirement(name="Album Api")
     public ResponseEntity<?> downloadThumbnail(@PathVariable("album_id") long album_id,@PathVariable("photo_id") long photo_id,Authentication authentication){

        return downloadFile(album_id, photo_id,THUMBNAIL_FOLDER_NAME, authentication);
     }

     
    public ResponseEntity<?> downloadFile(@PathVariable("album_id") long album_id,@PathVariable("photo_id") long photo_id,String folder_name,Authentication authentication){

        String email= authentication.getName();
        Optional<Account> optionalAccount=  accountService.findByEmail(email);
        Account acc= optionalAccount.get();
        Optional<Album> optionalAlbum= albumService.findById(album_id);

        if(!optionalAlbum.isPresent()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        }

        Album album= optionalAlbum.get();
        if(optionalAlbum.isPresent()){

            if(acc.getId()!= album.getAccount().getId()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        }

        Optional<Photo> optionalPhoto= photoService.findById(photo_id);

        if(optionalPhoto.isPresent()){

            Photo photo= optionalPhoto.get();
            if(photo.getAlbum().getId() !=album.getId()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            Resource resource=null;

            try {
                resource= AppUtil.getFileAsResource(album_id, folder_name,photo.getFileName());
            } catch (IOException e) {
                return ResponseEntity.internalServerError().build();
            }

            if(resource==null){
                return new ResponseEntity<>("File Not Found",HttpStatus.NOT_FOUND);
            }

            String contentType="application/octet-stream";
            String headerValue= "attachment; filename=\"" + photo.getOriginalFileName() + "\"";

            return ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(contentType))
                                .header(HttpHeaders.CONTENT_DISPOSITION,headerValue)
                                .body(resource);
            

        }

        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
     }
    
}
