INSERT INTO PRODUCT_BOARD (PRODUCT_ID, SELLER_ID, NICKNAME, TITLE, CONTENT, UNIT_PRICE,
                           CATEGORY_NAME, MAIN_IMAGE_URL, MAX_RENTAL_PERIOD, STATUS, WISH_REGION,
                           IMAGE_URLS, CREATED_AT,
                           MODIFIED_AT)
VALUES (1, 'test1@naver.com', 'test12', 'title', 'content',
        600, 'CLOTHING', '998f15eb-c565-4f87-a', 'ONEMONTH', 'RENTED', '서울시 중랑구',
        '0483b990-98a5-4cf6-9,a1bd0ce5-1d25-4ebe-8', '2023-03-20 20:44:56.97312',
        '2023-03-20 20:44:56.973163');

INSERT INTO PRODUCT_BOARD (PRODUCT_ID, SELLER_ID, NICKNAME, TITLE, CONTENT, UNIT_PRICE,
                           CATEGORY_NAME, MAIN_IMAGE_URL, MAX_RENTAL_PERIOD, STATUS, WISH_REGION,
                           IMAGE_URLS, CREATED_AT,
                           MODIFIED_AT)
VALUES (2, 'test2@naver.com', 'test12', 'title', 'content',
        600, 'CLOTHING', '998f15eb-c565-4f87-a', 'ONEMONTH', 'WAITING', '서울시 중랑구',
        '0483b990-98a5-4cf6-9,a1bd0ce5-1d25-4ebe-8', '2023-03-20 20:44:56.97312',
        '2023-03-20 20:44:56.973163');

INSERT INTO PRODUCT_BOARD (PRODUCT_ID, SELLER_ID, NICKNAME, TITLE, CONTENT, UNIT_PRICE,
                           CATEGORY_NAME, MAIN_IMAGE_URL, MAX_RENTAL_PERIOD, STATUS, WISH_REGION,
                           IMAGE_URLS, CREATED_AT,
                           MODIFIED_AT)
VALUES (3, 'test3@naver.com', 'test12', 'title', 'content',
        600, 'CLOTHING', '998f15eb-c565-4f87-a', 'ONEMONTH', 'RENTED', '서울시 중랑구',
        '0483b990-98a5-4cf6-9,a1bd0ce5-1d25-4ebe-8', '2023-03-20 20:44:56.97312',
        '2023-03-20 20:44:56.973163');

INSERT INTO PRODUCT_BOARD (PRODUCT_ID, SELLER_ID, NICKNAME, TITLE, CONTENT, UNIT_PRICE,
                           CATEGORY_NAME, MAIN_IMAGE_URL, MAX_RENTAL_PERIOD, STATUS, WISH_REGION,
                           IMAGE_URLS, CREATED_AT,
                           MODIFIED_AT)
VALUES (4, 'test4@naver.com', 'test12', 'title', 'content',
        600, 'CLOTHING', '998f15eb-c565-4f87-a', 'ONEMONTH', 'AVAILABLE', '서울시 중랑구',
        '0483b990-98a5-4cf6-9,a1bd0ce5-1d25-4ebe-8', '2023-03-20 20:44:56.97312',
        '2023-03-20 20:44:56.973163');

INSERT INTO PRODUCT_BOARD (PRODUCT_ID, SELLER_ID, NICKNAME, TITLE, CONTENT, UNIT_PRICE,
                           CATEGORY_NAME, MAIN_IMAGE_URL, MAX_RENTAL_PERIOD, STATUS, WISH_REGION,
                           IMAGE_URLS, CREATED_AT,
                           MODIFIED_AT)
VALUES (5, 'test5@naver.com', 'test12', 'title', 'content',
        600, 'CLOTHING', '998f15eb-c565-4f87-a', 'ONEMONTH', 'WAITING', '서울시 중랑구',
        '0483b990-98a5-4cf6-9,a1bd0ce5-1d25-4ebe-8', '2023-03-20 20:44:56.97312',
        '2023-03-20 20:44:56.973163');

INSERT INTO PRODUCT_BOARD (PRODUCT_ID, SELLER_ID, NICKNAME, TITLE, CONTENT, UNIT_PRICE,
                           CATEGORY_NAME, MAIN_IMAGE_URL, MAX_RENTAL_PERIOD, STATUS, WISH_REGION,
                           IMAGE_URLS, CREATED_AT,
                           MODIFIED_AT)
VALUES (6, 'test6@naver.com', 'test12', 'title', 'content',
        600, 'CLOTHING', '998f15eb-c565-4f87-a', 'ONEMONTH', 'RENTED', '서울시 중랑구',
        '0483b990-98a5-4cf6-9,a1bd0ce5-1d25-4ebe-8', '2023-03-20 20:44:56.97312',
        '2023-03-20 20:44:56.973163');

INSERT INTO PRODUCT_BOARD (PRODUCT_ID, SELLER_ID, NICKNAME, TITLE, CONTENT, UNIT_PRICE,
                           CATEGORY_NAME, MAIN_IMAGE_URL, MAX_RENTAL_PERIOD, STATUS, WISH_REGION,
                           IMAGE_URLS, CREATED_AT,
                           MODIFIED_AT)
VALUES (7, 'test7@naver.com', 'test12', 'title', 'content',
        600, 'CLOTHING', '998f15eb-c565-4f87-a', 'ONEMONTH', 'AVAILABLE', '서울시 중랑구',
        '0483b990-98a5-4cf6-9,a1bd0ce5-1d25-4ebe-8', '2023-03-20 20:44:56.97312',
        '2023-03-20 20:44:56.973163');


INSERT INTO RENTAL_HISTORY (ID, PRODUCT_ID, SELLER_ID, USER_ID, TOTAL_PRICE, RETURN_YN, RENTAL_DATE,
                            RETURN_DATE)
VALUES (1, 1, 'test1@naver.com', 'test12@naver.com', 6000, 0, '2023-03-20 20:44:56.97312',
        '2023-04-20 20:44:56.97312');


INSERT INTO MEMBER("ID", "EMAIL", "IMAGE_URL", "NICK_NAME", "PASSWORD", "REG_DATE",
                   "REGION", "TITLE", "UPDATE_DATE")
VALUES (1, 'test1@naver.com', '998f15eb-c565-4f87-a', 'test1', 'sdfsdff$$',
        '2023-03-20 20:44:56.97312',
        '서울시 도봉구', 'title', '2023-04-20 20:44:56.97312');