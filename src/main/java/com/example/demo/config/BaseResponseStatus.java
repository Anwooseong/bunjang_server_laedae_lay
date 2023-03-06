package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users/create
    POST_USERS_EMPTY_STORE_NAME(false, 2015, "상점명 입력칸이 비어 있습니다."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_PHONE(false,2017,"중복된 전화번호입니다."),
    POST_USERS_WITHDRAW_FAIL(false, 2018, "회원 탈퇴에 실패하였습니다."),
    POST_USERS_EXISTS_STORE_NAME(false, 2019, "중복된 가게이름입니다."),
    POST_USERS_EMPTY_PHONE_NUMBER(false, 2020, "전화번호 입력칸이 비어 있습니다."),
    POST_USERS_EMPTY_NAME(false, 2021, "이름 입력칸이 비어 있습니다."),
    POST_USERS_EMPTY_BIRTH(false, 2022, "생일 입력칸을 다시 확인해주세요."),
    POST_USERS_EMPTY_CARRIER(false, 2023, "통신사 입력칸을 다시 확인해주세요."),
    POST_USERS_COUNT_PHONE(false, 2024, "휴대폰 전화번호 4자리 이상 적어주세요."),
    POST_USERS_PHONE_REGEX(false, 2025, "휴대폰 전화번호 -개수와 숫자를 3, 4, 4로 맞춰주세요."),
    POST_USERS_BIRTH_REGEX(false, 2026, "생년월일 6자리 수를 확인하고, 월일이 각각 00이 안되게 해주세요."),
    POST_USERS_EMPTY_GENDER(false, 2027, "성별 입력칸이 비어있습니다."),
    POST_USERS_GENDER_REGEX(false, 2028, "성별은 숫자 4이하로 입력해주세요."),
    POST_USERS_REPORT_USER(false, 2029, "신고로 정지당한 회원입니다."),




    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PHONE_ENCRYPTION_ERROR(false, 4011, "전화번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),
    BIRTH_ENCRYPTION_ERROR(false, 4013, "생일 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
