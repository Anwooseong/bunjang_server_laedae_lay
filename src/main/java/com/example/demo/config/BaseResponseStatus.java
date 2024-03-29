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
    EXPIRED_TOKEN(false, 2004, "만료된 토큰입니다. 다시 로그인해주세요."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),
    USERS_NOT_EXISTED(false, 2011, "존재하지 않는 유저입니다."),

    // [POST] /users/create
    POST_USERS_EMPTY_STORE_NAME(false, 2015, "상점명 입력칸이 비어 있습니다."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_PHONE(false,2017,"중복된 전화번호입니다."),
    POST_USERS_WITHDRAW_FAIL(false, 2018, "회원 탈퇴에 실패하였습니다."),
    POST_USERS_EXISTS_STORE_NAME(false, 2019, "중복된 가게이름입니다."),
    POST_USERS_EMPTY_PHONE_NUMBER(false, 2020, "전화번호 입력칸이 비어 있습니다."),
    POST_USERS_EMPTY_NAME(false, 2021, "이름 입력칸이 비어 있습니다."),
    POST_USERS_EMPTY_UID(false, 2022, "아이디 입력칸이 비어 있습니다."),
    POST_USERS_EMPTY_PASSWORD(false, 2023, "비밀번호 입력칸이 비어 있습니다."),
    POST_USERS_COUNT_PHONE(false, 2024, "휴대폰 전화번호 4자리 이상 적어주세요."),
    POST_USERS_PHONE_REGEX(false, 2025, "휴대폰 전화번호 -개수와 숫자를 3, 4, 4로 맞춰주세요."),
    POST_USERS_PASSWORD_REGEX(false, 2026, "최소 8 자, 하나의 소문자, 하나의 숫자 및 하나의 특수 문자 정규식"),
    POST_USERS_EMPTY_GENDER(false, 2027, "성별 입력칸이 비어있습니다."),
    POST_USERS_GENDER_REGEX(false, 2028, "성별은 숫자 4이하로 입력해주세요."),
    POST_USERS_REPORT_USER(false, 2029, "신고로 정지당한 회원입니다."),
    POST_USERS_UID_REGEX(false, 2030, "영문과 숫자의 조합으로 5글자에서 8글자로 맞춰주세요."),
    POST_USERS_EXISTS_UID(false,2031,"중복된 아이디입니다."),
    GET_SEARCH_REGEX(false,2032,"검색창 첫 글자에 한글 자음이 있거나 한글 모음이 있습니다."),
    GET_BRAND_ORDER(false,2033,"쿼리 파라미터에서 order 부분을 다시 확인해주세요."),
    GET_BRAND_FOLLOWING(false,2034,"쿼리 파라미터에서 following 부분을 다시 확인해주세요."),
    GET_POINT_STATUS(false,2035,"쿼리 파라미터에서 status 부분을 다시 확인해주세요."),
    POST_REVIEW_EMPTY_CONTENT(false,2036,"리뷰 내용 입력칸이 비어있습니다."),
    POST_REVIEW_STAR_SIZE(false,2037,"별점은 0 이상 5 이하여야합니다."),
    POST_FOLLOW_EXISTS(false,2038,"이미 팔로우되어있습니다."),
    POST_ACCOUNT_MAXIMUM(false,2039,"계좌는 최대 2개까지 등록 가능합니다."),
    POST_ACCOUNT_EXISTS(false,2040,"이미 존재하는 계좌입니다."),
    POST_ACCOUNT_HOLDER_NAME_REGEX(false,2041,"예금주명은 한글 영어만 가능합니다."),
    POST_CREATE_ACCOUNT_FAIL(false,2042,"계좌 추가에 실패하였습니다."),
    POST_CREATE_DEFAULT_ACCOUNT_FAIL(false,2043,"계좌 기본값 설정에 실패하였습니다."),
    POST_ACCOUNT_NUMBER_REGEX(false,2044,"계좌 추가할 때 계좌 번호는 숫자만 입력해야합니다."),
    PATCH_ACCOUNT_NUMBER_REGEX(false,2045,"계좌 수정할 때 계좌 번호는 숫자만 입력해야합니다."),
    PATCH_MODIFY_ACCOUNT_FAIL(false,2046,"계좌 수정에 실패하였습니다."),
    PATCH_MODIFY_DEFAULT_ACCOUNT_FAIL(false,2047,"계좌 기본값 설정 수정에 실패하였습니다."),
    PATCH_DELETE_ACCOUNT_FAIL(false,2048,"계좌 삭제에 실패하였습니다."),
    PATCH_LAST_ACCESS_DATE_RENEW_FAIL(false,2049,"최근 접속일 갱신에 실패하였습니다."),
    PATCH_LIKE_CANCEL_FAIL(false,2050,"찜 취소에 실패하였습니다."),
    POST_PAYMENT_POINT_LITTLE(false,2051,"사용 가능한 번개 포인트 이하의 값을 입력해야합니다."),
    POST_PAYMENT_AGREEMENT(false,2052,"개인정보 제 3자 제공동의와 결제대행 서비스 이용 약관 동의를 확인해주세요."),
    POST_PAYMENT_METHOD_EMPTY(false,2053,"결제 수단을 입력해주세요."),
    POST_PRODUCT_SOLD_OUT(false,2054,"재고가 없는 상품입니다."),
    GET_DELIVERY_PARAMETER(false,2055,"쿼리 파라미터에서 status가 sell, receive, register 중에 있어야합니다."),
    POST_MY_PRODUCT_EXISTS(false,2056,"이미 찜 목록에 추가되어있는 상품입니다."),
    GET_SEARCH_PRODUCT_PARAMETER(false,2057,"쿼리 파라미터에서 order이 recent(최신순), price-asc(저가순), price-desc(고가순) 중에 있어야합니다."),
    GET_HISTORY_PARAMETER_TYPE(false,2058,"쿼리 파라미터에서 type은 sale(판매), purchase(구매) 중에 있어야합니다."),
    GET_HISTORY_PARAMETER_STATUS(false,2059,"쿼리 파라미터에서 status는 all(전체 상태), progress(진행중), complete(완료), cancel-refund(취소/환불) 중에 있어야합니다."),
    GET_HISTORY_PARAMETER_PAY(false,2060,"쿼리 파라미터에서 pay는 all(전체 거래), progress(번개 페이 안전결제) 중에 있어야합니다."),
    GET_CHAT_PARAMETER(false,2061,"쿼리 파라미터에서 type은 all(전체 대화), sale(판매 대화), purchase(구매 대화) 중에 있어야합니다."),
    POST_PRODUCT_IMAGE_FAIL(false,2062,"상품 이미지 등록에 실패하였습니다."),
    POST_PRODUCT_TAG_FAIL(false,2063,"상품 태그 등록에 실패하였습니다."),
    POST_PRODUCT_IMAGE_SIZE(false,2064,"이미지는 최대 5개까지만 올릴 수 있습니다."),
    POST_PRODUCT_AMOUNT_POSITIVE(false,2065,"상품 수량은 0보다 커야합니다."),
    POST_PRODUCT_TAG_SIZE(false,2066,"태그는 최대 6개까지만 등록할 수 있습니다."),
    POST_PRODUCT_IMAGE_UPLOAD(false,2067,"이미지 업로드에 실패하였습니다."),
    GET_PRODUCT_NOT_EXISTED(false, 2068, "존재하지 않는 상품 ID입니다."),
    PATCH_PRODUCT_STATUS_INVALID_OR_EMPTY_PARAMETER(false, 2069, "쿼리 스트링 값이 없거나 부적절한 값 입니다. for-sale, reserved, sold-out 중 하나의 값을 가져야 합니다."),
    MODIFY_PRODUCT_STATUS_FAIL(false, 2070, "상품 상태 수정에 실패하였습니다."),
    GET_TAG_QUERY_PARAMETER(false, 2071, "쿼리 파라미터에 query부분이 null로 들어왔습니다."),
    GET_PRODUCT_QUERY_PARAMETER(false, 2072, "쿼리 파라미터에 query부분이 null로 들어왔습니다."),
    GET_PRODUCT_ORDER_PARAMETER(false, 2073, "쿼리 파라미터에 order부분이 null로 들어왔습니다."),
    GET_PRODUCT_INVALID_QUERY_STRING_ORDER(false, 2074, "Query String에서 order 값이 부적절합니다. recent(최신순), popular(인기순), low(저가순), high(고가순) 중의 하나의 값을 가져야합니다."),
    GET_PRODUCT_INVALID_QUERY_STRING_STATUS(false, 2075, "Query String에서 status 값이 부적절합니다. for-sale(판매중), all(전체), reserved(예약중), sold-out(판매완료), pay-avail(페이가능), ad(광고중) 중의 하나의 값을 가져야합니다."),
    POST_USER_ADDRESS_CREATE_FAIL(false, 2076, "배송지 추가 요청이 실패하였습니다."),
    PATCH_USER_ADDRESS_UPDATE_FAIL(false, 2077, "배송지 수정 요청이 실패하였습니다"),
    PATCH_USER_ADDRESS_DELETE_FAIL(false, 2078, "배송지 삭제 요청이 실패하였습니다."),
    PATCH_USER_PRODUCT_UPDATE_FAIL(false, 2079, "상품 정보 수정에 실패하였습니다."),
    PATCH_PRODUCT_IMG_DELETE_ALL_FAIL(false, 2080, "상품 이미지 전체 삭제에 실패하였습니다."),
    PATCH_USER_PRODUCT_IMG_CREATE_FAIL(false, 2081, "상품 이미지 리스트 등록에 실패하였습니다."),
    PATCH_USER_PRODUCT_TAG_DELETE_ALL_FAIL(false, 2082, "상품의 태그 리스트 전체 삭제에 실패하였습니다."),



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

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


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
