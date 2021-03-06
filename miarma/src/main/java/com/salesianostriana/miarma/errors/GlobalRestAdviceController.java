package com.salesianostriana.miarma.errors;

import com.salesianostriana.miarma.errors.exceptions.entitynotfound.EntityNotFoundException;
import com.salesianostriana.miarma.errors.exceptions.following.FollowingSelfException;
import com.salesianostriana.miarma.errors.exceptions.following.PrivateProfileException;
import com.salesianostriana.miarma.errors.exceptions.storage.WrongFormatException;
import com.salesianostriana.miarma.errors.model.ApiError;
import com.salesianostriana.miarma.errors.model.ApiSubError;
import com.salesianostriana.miarma.errors.model.ApiValidationSubError;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalRestAdviceController extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return this.buildApiError(ex, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {


        List<ApiSubError> subErrorList = new ArrayList<>();

        ex.getAllErrors().forEach(error -> {

            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;

                subErrorList.add(
                        ApiValidationSubError.builder()
                                .objeto(fieldError.getObjectName())
                                .campo(fieldError.getField())
                                .valorRechazado(fieldError.getRejectedValue())
                                .mensaje(fieldError.getDefaultMessage())
                                .build()
                );
            }
            else
            {
                ObjectError objectError = (ObjectError) error;

                subErrorList.add(
                        ApiValidationSubError.builder()
                                .objeto(objectError.getObjectName())
                                .mensaje(objectError.getDefaultMessage())
                                .build()
                );
            }


        });



        return buildApiError(ex, HttpStatus.BAD_REQUEST, request,
                subErrorList.isEmpty() ? null : subErrorList

        );
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {

        return buildApiError(ex, HttpStatus.BAD_REQUEST, request, ex.getConstraintViolations()
                .stream()
                .map(cv -> ApiValidationSubError.builder()
                        .objeto(cv.getRootBeanClass().getSimpleName())
                        .campo(((PathImpl) cv.getPropertyPath()).getLeafNode().asString())
                        .valorRechazado(cv.getInvalidValue())
                        .mensaje(cv.getMessage())
                        .build()).collect(Collectors.toList()));
    }

    @ExceptionHandler({WrongFormatException.class})
    public ResponseEntity<?> handleWrongFormatException(WrongFormatException e, WebRequest request){

        return buildApiError(e, HttpStatus.BAD_REQUEST, request);

    }


    @ExceptionHandler({PrivateProfileException.class})
    public ResponseEntity<?> handlePrivateProfileException(PrivateProfileException e, WebRequest request){

        return buildApiError(e, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler({FollowingSelfException.class})
    public ResponseEntity<?> handleFollowingSelfException(FollowingSelfException e, WebRequest request){

        return buildApiError(e, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<?> handleNotFoundException(EntityNotFoundException e, WebRequest request) {

        return this.buildApiError(e, HttpStatus.NOT_FOUND, request);


    }

    @ExceptionHandler({EntityExistsException.class})
    public ResponseEntity<?> handleRequestAlreadySentException(EntityExistsException e, WebRequest request){

        return buildApiError(e,"La petici??n a??n est?? por confirmarse" ,HttpStatus.resolve(409), request);

    }

    //MENSAJES
    private String getMessageForLocale(String messageKey, Locale locale){

        return ResourceBundle.getBundle("errors", locale).getString(messageKey);

    }

    ///BUILD API ERROR
    //Con sub errors
    private ResponseEntity<Object> buildApiError(Exception exception, HttpStatus status, WebRequest request, List<ApiSubError> subErrorList) {



        ApiError error = ApiError.builder()
                .estado(status)
                .codigo(status.value())
                .ruta(((ServletWebRequest) request).getRequest().getRequestURI())
                //TODO: Internacionalizar este mensaje
                .mensaje("Hubo errores en la validaci??n")
                .apiSubErrors(subErrorList)
                .build();

        return ResponseEntity.status(status).body(error);
    }

    //Sin sub errors
    private ResponseEntity<Object> buildApiError(Exception exception, HttpStatus status, WebRequest request) {

        ApiError error = ApiError.builder()
                .estado(status)
                .codigo(status.value())
                .ruta(((ServletWebRequest) request).getRequest().getRequestURI())
                .mensaje(exception.getMessage())
                .build();

        return ResponseEntity.status(status).body(error);
    }

    private ResponseEntity<Object> buildApiError(Exception exception, String customMessage ,HttpStatus status, WebRequest request) {

        ApiError error = ApiError.builder()
                .estado(status)
                .codigo(status.value())
                .ruta(((ServletWebRequest) request).getRequest().getRequestURI())
                .mensaje(customMessage)
                .build();

        return ResponseEntity.status(status).body(error);
    }


}
