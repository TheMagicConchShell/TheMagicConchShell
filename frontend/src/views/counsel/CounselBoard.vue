<template>
    <div>
        <v-wait for="board list load">
            <template slot="waiting">
                <infinite-loading />
            </template>
            <ul class="fz_list">
                <li class="fz_list_th">
                    <div
                        class="fz_num hidden-xs"
                    >
                        {{ $t('board.no') }}
                    </div>
                    <div
                        class="fz_subject"
                    >
                        {{ $t('board.title') }}
                        <!-- 제목 -->
                    </div>
                    <div
                        class="fz_hit hidden-xs"
                    >
                        {{ $t('board.views') }}
                        <!-- 조회 -->
                    </div>
                    <div
                        class="fz_writer hidden-xs"
                    >
                        {{ $t('board.writer') }}
                        <!-- 작성자 -->
                    </div>
                    <div
                        class="fz_date hidden-xs"
                    >
                        {{ $t('board.date') }}
                        <!-- 날짜 -->
                    </div>
                </li>
                <template v-if="!list">
                    <div
                        class=""
                    >
                        이런 사이트가 망해서 고민이 없습니다...
                    </div>
                </template>
                <li
                    v-for="item in list"
                    :key="item.no"
                >
                    <div class="fz_num hidden-xs">
                        {{ item.no }}
                    </div>
                    <div class="fz_subject">
                        <router-link
                            :to="{name: 'CounselDetail', params: {no: item.no}}"
                        >
                            [{{ item.category }}] {{ item.title }}
                        </router-link>
                    </div>
                    <div class="fz_hit">
                        {{ item.views }}
                    </div>
                    <div class="fz_writer">
                        {{ item.writer }}
                    </div>
                    <div class="fz_date">
                        {{ item.writeDate }}
                    </div>
                </li>
            </ul>
        </v-wait>
        <template v-if="list && list.length">
            <CounselSelectPaginate
                :current="page"
                :last="pageCount"
                :page-handle="pageHandle"
            />
        </template>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import CounselSelectPaginate from "@/components/CounselSelectPaginate";

export default {
    components: {
        CounselSelectPaginate,
    },
    props: {
        category: {
            type: String,
            default: '전체',
        },
        page: {
            type: Number,
            default: 1,
        }
    },
    data: () => ({
        list: [],
        pageCount: 1,
    }),
    computed: {
        ...mapGetters(['language']),
    },
    watch: {
        category() {
            this.fetchPost(this.page);
        },
        page() {
            this.fetchPost(this.page);
        },
        language() {
            this.fetchPost(this.page);
        },
    },
    async created() {
        await this.fetchPost(this.page);
    },
    methods: {
        fetchPost(page) {
            this.$wait.start("board list load");
            this.$store.dispatch('fetchCategories');
            this.list = null;

            if (this.category === '전체') {
                this.$axios({
                    url: '/counsel/post',
                    method: 'get',
                    params: {
                        page: page || 1,
                    }
                })
                    .then((response) => {
                        if (200 <= response.status && response.status < 300) {
                            let formatDate = function (date) {
                                let d = new Date(date),
                                    month = '' + (d.getMonth() + 1),
                                    day = '' + d.getDate(),
                                    year = d.getFullYear();

                                if (month.length < 2) 
                                    month = '0' + month;
                                if (day.length < 2) 
                                    day = '0' + day;

                                return [month, day].join('-');
                            };
                            this.pageCount = response.data.data.totalPages;
                            this.list = response.data.data.content.map((e) => {
                                e.writeDate = formatDate(e.writeDate);
                                e.category = this.$store.getters.categoryNameById(e.categoryId, this.language);
                                return e;
                            });
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    })
                    .finally(() => {
                        this.$wait.end("board list load");
                    });

            } else {
                this.$axios({
                    url: '/counsel/post/category',
                    method: 'get',
                    params: {
                        page: page || 1,
                        category: this.category,
                    }
                })
                    .then((response) => {
                        if (200 <= response.status && response.status < 300) {
                            let formatDate = function (date) {
                                let d = new Date(date),
                                    month = '' + (d.getMonth() + 1),
                                    day = '' + d.getDate(),
                                    year = d.getFullYear();

                                if (month.length < 2) 
                                    month = '0' + month;
                                if (day.length < 2) 
                                    day = '0' + day;

                                return [month, day].join('-');
                            };
                            this.pageCount = response.data.data.totalPages;
                            this.list = response.data.data.content.map((e) => {
                                e.writeDate = formatDate(e.writeDate);
                                e.category = this.$store.getters.categoryNameById(e.categoryId, this.language);
                                return e;
                            });
                        }
                    })
                    .catch((error) => {
                        console.log(error);
                    })
                    .finally(() => {
                        this.$wait.end("board list load");
                    });
            }
        },
        pageHandle(nextPage) {
            this.$router.push({
                name: 'List',
                query: {
                    page: nextPage,
                },
            }).catch(() => {});
        },
    },
};
</script>

<style scoped>
@charset "utf-8";
/* @import url(//fonts.googleapis.com/earlyaccess/nanumgothic.css); */

a {
    color: unset;
}
a:hover {
    text-decoration: unset;
}

/* 게시판 기본 설정 */

#container_title {
    display: none;
}

.fz_wrap,
.fz_wrap h1,
.fz_wrap h2,
.fz_wrap h3,
.fz_wrap h4,
.fz_wrap h5,
.fz_wrap h6,
.fz_wrap input,
.fz_wrap button,
.fz_wrap textarea,
.fz_wrap select,
.fz_wrap .bo_current {
    /* font-family: Nanum Gothic; */
    color: #333;
}

.fz_wrap a:hover {
    text-decoration: none;
}

.fz_wrap .f_wrap {
    *zoom: 1;
}

.fz_wrap .f_wrap:after {
    content: "";
    display: block;
    clear: both;
}

.fz_wrap .fl {
    float: left;
}

.fz_wrap .fr {
    float: right;
}

.fz_wrap legend {
    border: none !important;
}

.i_text {
    height: 22px;
    padding: 0 3px;
    line-height: 20px;
    font-size: 14px;
    color: #333;
    background: none;
    border: 1px solid #b7b7b7;
    border-right-color: #e1e1e1;
    border-bottom-color: #e1e1e1;
}

.i_text_large {
    display: inline-block;
    height: 31px;
    padding: 4px 6px;
    font-size: 14px;
    line-height: 20px;
    color: #333;
    border: 1px solid #b7b7b7;
    border-right-color: #e1e1e1;
    border-bottom-color: #e1e1e1;
    vertical-align: top;
    width: 218px;
    background: none;
}

.fz_title_box {
    font-size: 14px;
    font-weight: bold;
    color: #515151;
    position: relative;
    background: #f3f3f3;
    /* background: url("./img/fz_title_bg.png") 0 0 repeat-x; */
    height: 35px;
    line-height: 35px;
    border: 1px solid #dedede;
    padding: 0px 15px;
    font-weight: 700;
    margin-bottom: 20px;
}

textarea#wr_content {
    border: 1px solid #ccc;
    height: 200px;
}

#captcha #captcha_info {
    display: none;
}

.fz_wrap .required {
    background-color: #fff !important;
}

.fz_wrap input[type="text"],
.fz_wrap input[type="password"],
.fz_wrap input[type="submit"],
.fz_wrap input[type="search"],
.fz_wrap input[type="image"] {
    -webkit-border-radius: 0;
    border-radius: 0;
    -webkit-appearance: none;
}


/* safari 둥근모서리 그림자 제거 */

.bo_fx {
    margin-bottom: 5px;
    zoom: 1
}

.bo_fx:after {
    display: block;
    visibility: hidden;
    clear: both;
    content: ""
}

.bo_fx ul {
    margin: 0;
    padding: 0;
    list-style: none
}

#bo_list_total {
    float: left;
    padding-top: 5px
}

.btn_bo_user {
    float: right;
    margin: 0;
    padding: 0;
    list-style: none
}

.btn_bo_user li {
    float: left;
    margin-left: 5px
}

.btn_bo_adm {
    float: left
}

.btn_bo_adm li {
    float: left;
    margin-right: 5px
}

.btn_bo_adm input {
    padding: 8px;
    border: 0;
    background: #e8180c;
    color: #fff;
    text-decoration: none;
    vertical-align: middle
}

.bo_notice td {
    background: #fffaf2
}

.bo_notice td a {
    font-weight: 700
}

.td_num strong {
    color: #000
}

.bo_cate_link {
    display: inline-block;
    margin: 0 6px 0 0;
    padding: 0 6px 0 0;
    border-right: 1px solid #e7f1ed;
    color: #555 !important;
    font-weight: 700;
    text-decoration: none;
}


/* 글제목줄 분류스타일 */

.bo_current {
    color: #e8180c !important;
    font-size: 14px;
}


/* 게시판 카테고리 */

#bo_cate h2 {
    position: absolute;
    font-size: 0;
    line-height: 0;
    overflow: hidden;
}

#bo_cate ul {
    margin-bottom: 10px;
    padding-left: 1px;
    zoom: 1;
    font-size: 14px;
    list-style: none
}

#bo_cate ul:after {
    display: block;
    visibility: hidden;
    clear: both;
    content: ""
}

#bo_cate li {
    float: left;
    margin-top: -1px;
}

#bo_cate a {
    display: block;
    position: relative;
    margin-left: -1px;
    padding: 8px 15px;
    color: #888;
    text-align: center;
    letter-spacing: -0.1em;
    line-height: 1.2em;
    cursor: pointer;
    border: 1px solid #ddd;
}

#bo_cate a:focus,
#bo_cate a:hover,
#bo_cate a:active {
    text-decoration: none
}

#bo_cate #bo_cate_on {
    z-index: 2;
    border: 1px solid #0e8185;
    background: #0e8185;
    color: #fff;
    font-weight: 700
}

#bo_cate a:hover {
    z-index: 2;
    border: 1px solid #23ADB2;
    background: #fff;
    color: #23adb2;
}


/* 리스트 상단 */

.fz_header {
    *zoom: 1;
    margin-bottom: 8px;
    padding-top: 10px;
}

.fz_header:after {
    content: "";
    display: block;
    clear: both;
}

.fz_total_count {
    float: left;
}

.fz_rss {
    float: right;
}

.fz_total_count span {
    padding-left: 21px;
    font-weight: 700;
    color: #333;
    /* background: url('./img/sprites_ico.gif') no-repeat 0 -24px; */
    font-size: 14px;
}

.fz_total_count strong {
    color: #0e8185;
    font-weight: 700;
}

.fz_list {
    list-style: none;
    padding: 0;
    margin: 0;
    width: 100%;
    display: table;
    font-size: 14px;
    border-top: 2px solid #808080;
}

.fz_list li.fz_list_th {
    background: lightgray;
    /* background: url(./img/list_top_bar.gif) 0 0 repeat-x; */
    height: 38px;
}

.fz_list li.fz_list_th div {
    text-align: center;
    vertical-align: middle;
    color: #101010;
    font-weight: bold;
    font-size: 14px !important;
    height: 38px;
    line-height: 25px;
}

.fz_list li.fz_list_th .fz_subject,
.fz_list li.fz_list_th .fz_checkbox {
    line-height: 38px;
}

.fz_list li.fz_list_th .fz_checkbox {
    padding: 0;
}

.fz_list li.fz_list_th a {
    color: #0e8185 !important;
}

.fz_list li div .sv_wrap .sv {
    text-align: left;
}

.fz_list li div input {
    margin: 0;
}

.fz_list li.bg_e div .sv_wrap .sv a {
    color: #fff !important
}

.fz_list li {
    display: block;
    border-bottom: 1px solid #dadada;
    position: relative;
}

.fz_list li.bo_notice {
    background: #fffaf2;
}

.fz_list li.bg_e {
    background: #f5f5f5;
}

.fz_list li.bg_e div,
.fz_list li.bg_e div * {
    color: #23ADB2 !important;
}

.fz_list li div {
    display: inline-block;
    text-align: center;
    height: 33px;
    line-height: 22px;
    padding: 6px 0;
    vertical-align: middle;
    word-break: break-all;
}

.fz_list li .fz_checkbox {
    position: absolute;
    left: 13px;
    top: -2px;
}

.fz_list li .fz_subject {
    display: block;
    text-align: left;
    height: auto;
    padding: 0;
}

.fz_list li .fz_subject a {
    display: block;
    line-height: 33px;
    line-height: 21px;
    padding: 6px 10px 6px 15px;
    /* vertical-align: middle; */
    word-break: break-all
}

.fz_list li .fz_mobile_info {
    display: inline-block;
    margin-right: 5px;
    font-size: 14px;
    color: #333
}

.fz_list li .fz_writer,
.fz_list li .fz_date,
.fz_list li .fz_hit,
.fz_list li .fz_good,
.fz_list li .fz_nogood {
    width: auto;
    display: inline-block;
    padding: 0 0 0 15px;
}

.fz_list li .fz_num,
.fz_list li .fz_date,
.fz_list li .fz_hit,
.fz_list li .fz_good,
.fz_list li .fz_nogood {
    font-size: 14px;
    /* font-family: tahoma; */
}

.fz_list li.re1 {
    padding-left: 0;
}

.fz_list li.re2 {
    padding-left: 18px;
}

.fz_list li.re3 {
    padding-left: 36px;
}

.fz_list li.re4 {
    padding-left: 54px;
}

.fz_list li.re5 {
    padding-left: 72px;
}

.fz_list li.re6 {
    padding-left: 90px;
}

.fz_list li.re7 {
    padding-left: 108px;
}

.fz_list li.re8 {
    padding-left: 126px;
}

.fz_list .fz_empty_list {
    width: 100%;
    padding: 30px 0;
    height: auto;
    line-height: 100px;
    display: table-caption;
    caption-side: bottom;
    text-align: center;
    border-bottom: 1px solid #dadada;
}

@media (min-width:768px) {
    .fz_list li.fz_list_th .fz_subject,
    .fz_list li.fz_list_th .fz_checkbox {
        line-height: 25px;
    }
    .fz_list li {
        display: table-row;
    }
    .fz_list li div {
        display: table-cell;
        text-align: center;
        height: 33px;
        line-height: 20px;
        padding: 6px 0;
        vertical-align: middle;
        border-bottom: 1px solid #dadada;
        word-break: break-all;
    }
    .fz_list li.bo_notice div {
        background: #fffaf2
    }
    .fz_list li.bg_e div {
        background: #f5f5f5;
    }
    .fz_list li .fz_num {
        width: 50px;
    }
    .fz_list li .fz_checkbox {
        width: 20px;
        position: static;
        left: 0;
        top: 0
    }
    .fz_list li .fz_subject {
        display: table-cell;
    }
    .fz_list li .fz_writer {
        width: 90px;
    }
    .fz_list li .fz_date {
        width: 70px;
    }
    .fz_list li .fz_hit {
        width: 45px;
    }
    .fz_list li .fz_good {
        width: 45px;
    }
    .fz_list li .fz_nogood {
        width: 45px;
    }
    .fz_list li .fz_mobile_info {
        display: none;
    }
    .fz_list li .fz_writer,
    .fz_list li .fz_date,
    .fz_list li .fz_hit,
    .fz_list li .fz_good,
    .fz_list li .fz_nogood {
        display: table-cell;
        padding: 6px 0;
    }
    .fz_list li.re1,
    .fz_list li.re2,
    .fz_list li.re3,
    .fz_list li.re4,
    .fz_list li.re5,
    .fz_list li.re6,
    .fz_list li.re7,
    .fz_list li.re8 {
        padding-left: 0;
    }
    /* 답변시 들여쓰기 */
    .fz_list li.re1 .icon_reply {
        margin-left: 0;
    }
    .fz_list li.re2 .icon_reply {
        margin-left: 18px;
    }
    .fz_list li.re3 .icon_reply {
        margin-left: 36px;
    }
    .fz_list li.re4 .icon_reply {
        margin-left: 54px;
    }
    .fz_list li.re5 .icon_reply {
        margin-left: 72px;
    }
    .fz_list li.re6 .icon_reply {
        margin-left: 90px;
    }
    .fz_list li.re7 .icon_reply {
        margin-left: 108px;
    }
    .fz_list li.re8 .icon_reply {
        margin-left: 126px;
    }
}


/* 리스트 하단 */

.fz_footer {
    *zoom: 1;
    margin-top: 10px;
}

.fz_footer:after {
    content: "";
    display: block;
    clear: both;
}


/* 리스트 검색 */

#bo_sch {
    margin-bottom: 10px;
    padding-top: 5px;
    text-align: center
}

#bo_sch legend {
    position: absolute;
    margin: 0;
    padding: 0;
    font-size: 0;
    line-height: 0;
    text-indent: -9999em;
    overflow: hidden;
    border: none;
}

#bo_sch .i_text {
    height: 27px;
    padding: 0px 5px;
    font-size: 14px;
    line-height: 27px;
    color: #333;
    border: 1px solid #ccc;
    vertical-align: middle;
}

#bo_sch .btn_search_submit {
    background: none !important;
    height: 27px !important;
    padding: 0 !important;
}


/* 아이콘 및 답변 */

.cnt_cmt {
    padding-left: 5px;
    color: #FF4800;
    /* font-family: tahoma; */
    font-size: 9px;
    margin: 0;
}

.icon_notice {
    display: inline-block;
    width: 10px;
    height: 19px;
    /* background: url('./img/sprites_ico.gif') no-repeat 0 -985px; */
    text-indent: -9000px;
    *zoom: 1;
    vertical-align: top;
}

.icon_pack {
    display: inline-block;
    margin-left: 5px;
    width: 9px;
    height: 19px;
    /* background: url('./img/sprites_ico.gif') no-repeat; */
    text-indent: -9000px;
}

.icon_pack2 {
    display: inline-block;
    width: 19px;
    height: 19px;
    font-size: 11px;
    /* background: url('./img/sprites_ico.gif') no-repeat; */
    text-indent: -9000px;
    overflow: hidden;
    vertical-align: top;
}

.icon_reply {
    width: 20px;
    background-position: 0 -437px;
}

.icon_secret2 {
    background-position: 0 -1005px;
}

.icon_file2 {
    background-position: 0 -1024px;
}

.icon_txt2 {
    background-position: 0 -1043px;
}

.icon_secret {
    background-position: 0 -1005px;
}

.icon_file {
    background-position: 0 -1024px;
}

.icon_link {
    background-position: 0 -1062px;
}

.icon_new {
    background-position: 0 -1081px;
}


/* Button List */

.list_btn {
    display: inline-block;
    height: 24px;
    /* background: url('./img/sprites_btn.gif') no-repeat; */
    text-indent: -9000px;
    overflow: hidden;
}

.list_btn.btn_list {
    width: 41px;
    background-position: -346px 0;
}

.list_btn.btn_copy {
    width: 41px;
    background-position: -603px 0;
}

.list_btn.btn_move {
    width: 41px;
    background-position: -649px 0;
}

.list_btn.btn_edit {
    width: 41px;
    background-position: -695px 0;
}

.list_btn.btn_del {
    width: 41px;
    background-position: -741px 0;
}

.list_btn.btn_reply {
    width: 41px;
    background-position: -787px 0;
}

.list_btn.btn_write {
    width: 65px;
    background-position: -833px 0;
}

.list_btn.btn_adm {
    width: 49px;
    height: 24px;
    /* background: url('./img/sprites_btn.gif') no-repeat -1719px 0; */
    text-indent: -9000px;
    overflow: hidden;
}

.list_btn.btn_rss {
    margin-right: 1px;
    width: 54px;
    background-position: -1336px 0;
    vertical-align: middle;
}

.list_btn.btn_prev {
    width: 50px;
    background-position: -392px 0;
    margin-right: 1px;
}

.list_btn.btn_next {
    width: 50px;
    background-position: -447px 0;
}

.list_btn.btn_track {
    width: 64px;
    background-position: -972px 0;
}

.list_btn.btn_scrap {
    width: 64px;
    background-position: -903px 0;
    vertical-align: middle;
}


/* 게시판 쓰기 */

#char_count_desc {
    display: block;
    margin: 0 0 5px;
    padding: 0
}

#char_count_wrap {
    margin: 5px 0 0;
    text-align: right
}

#char_count {
    font-weight: 700
}

#autosave_wrapper {
    position: relative
}

#autosave_pop {
    display: none;
    z-index: 10;
    position: absolute;
    top: 24px;
    right: 117px;
    padding: 8px;
    width: 350px;
    height: auto !important;
    height: 180px;
    max-height: 180px;
    border: 1px solid #565656;
    background: #fff;
    overflow-y: scroll
}

html.no-overflowscrolling #autosave_pop {
    height: auto;
    max-height: 10000px !important
}


/* overflow 미지원 기기 대응 */

#autosave_pop strong {
    position: absolute;
    font-size: 0;
    line-height: 0;
    overflow: hidden
}

#autosave_pop div {
    text-align: right
}

#autosave_pop button {
    margin: 0;
    padding: 0;
    border: 0;
    background: transparent
}

#autosave_pop ul {
    margin: 10px 0;
    padding: 0;
    border-top: 1px solid #e9e9e9;
    list-style: none
}

#autosave_pop li {
    padding: 8px 5px;
    border-bottom: 1px solid #e9e9e9;
    zoom: 1
}

#autosave_pop li:after {
    display: block;
    visibility: hidden;
    clear: both;
    content: ""
}

#autosave_pop a {
    display: block;
    float: left
}

#autosave_pop span {
    display: block;
    float: right
}

.autosave_close {
    cursor: pointer
}

.autosave_content {
    display: none
}


/* 게시판 뷰 페이지 상단 */

#bo_v {
    margin-bottom: 20px;
    padding-bottom: 20px
}

#bo_v h1#bo_v_title {
    border-top: 2px solid #52596B;
    border-bottom: 1px solid #c8c8c6;
    background: #f8f8f8;
    padding: 18px 15px;
    font-size: 18px;
    font-weight: 700;
    margin: 0 !important;
}

#bo_v_info {
    padding: 0px;
    border-bottom: 1px solid #ddd;
    *zoom: 1;
    background: #fff;
}

#bo_v_info:after {
    content: "";
    display: block;
    clear: both;
}

#bo_v_info span {
    line-height: 40px;
    height: 40px;
}

#bo_v_info span.sv {
    line-height: normal;
    height: auto;
    top: 16px;
}

#bo_v_info .fl {
    padding-left: 0px;
}

#bo_v_info .fr {
    padding-right: 0px;
}

#bo_v_info .fr i {
    font-size: 13px;
}

#bo_v_info .bo_v_user {
    font-size: 14px;
}

#bo_v_info .bo_v_user i {
    position: relative;
    float: left;
    line-height: 40px;
    margin-right: 5px;
}

#bo_v_info .sv_wrap {
    margin-top: 0;
    vertical-align: top;
}

#bo_v_info .sv_wrap a {
    line-height: normal;
    height: auto;
}

#bo_v_info .bo_v_user em {
    font-size: 10px;
    /* font-family: tahoma; */
    color: #aaa;
    padding-left: 5px;
    font-style: normal
}

#bo_v_info h2 {
    position: absolute;
    font-size: 0;
    line-height: 0;
    overflow: hidden
}

#bo_v_info .sv_member,
#bo_v_info .sv_guest,
#bo_v_info .member,
#bo_v_info .guest {
    font-weight: 700
}

#bo_v_info .bo_v_date,
#bo_v_info .bo_v_hit,
#bo_v_info .bo_v_comment {
    font-size: 11px;
    color: #333
}

#bo_v_info .bo_v_date strong,
#bo_v_info .bo_v_hit strong,
#bo_v_info .bo_v_comment strong {
    /* font-family: tahoma; */
    font-size: 10px;
    color: #777;
    font-weight: normal;
}

#bo_v_info .bar {
    color: #aaa;
    margin: 0 5px;
    font-size: 11px;
}


/* FILE, LINK 출력 부분 */

.ad_list_area {
    margin: 0;
    padding: 0;
    border-bottom: 1px solid #e4e4e4;
    font-size: 14px;
}

.ad_list_area li {
    clear: both;
    margin: 0;
    padding: 10px 20px 9px 20px;
    *height: auto;
    list-style: none;
    border-top: 1px solid #f3f3f3;
}

.ad_list_area li:hover {
    background: #f9f9f9;
}

.ad_list_area li:after {
    content: "";
    display: block;
    clear: both;
}

.ad_list_area li.first {
    border-top: none;
}

.ad_list_area li a {
    float: left;
    padding: 0 0 0 22px;
    line-height: 14px;
    /* background: url('./img/sprites_ico.gif') no-repeat; */
}

.ad_list_area li a.txt_name {
    color: #666;
    background-position: 0 -94px;
}

.ad_list_area li a.txt_name:hover {
    background-position: 0 -117px;
    text-decoration: underline;
}

.ad_list_area li a.txt_link {
    color: #0066cc;
    background-position: 0 -184px;
}

.ad_list_area li a.txt_link:hover {
    background-position: 0 -207px;
    text-decoration: underline;
}

.ad_list_area li span {
    float: left;
    line-height: 14px;
    font-size: 9px;
    /* font-family: tahoma; */
}

.ad_list_area li .txt_size {
    padding-left: 9px;
}

.ad_list_area li .txt_hit {
    padding-left: 7px;
    color: #ff4200;
}

.ad_list_area li .txt_date {
    padding-left: 10px;
}

.ad_list_area li .btn_handle {
    float: right;
    padding: 0;
    width: 14px;
    background-position: 3px -388px;
    text-indent: -9000px;
    overflow: hidden;
}

.ad_list_area li .btn_handle.on {
    background-position: 3px -403px;
}

.ad_list_area li p.txt_file_content {
    clear: both;
    margin: 0;
    padding: 8px 0 0 22px;
    font-size: 11px;
    word-wrap: break-word;
}


/* 게시판 뷰 버튼 리스트 */

#bo_v_top {
    margin: 0 0 10px;
    zoom: 1
}

#bo_v_top:after {
    display: block;
    visibility: hidden;
    clear: both;
    content: ""
}

#bo_v_top h2 {
    position: absolute;
    font-size: 0;
    line-height: 0;
    overflow: hidden
}

#bo_v_top ul {
    margin: 0;
    padding: 0;
    list-style: none
}

#bo_v_bot {
    zoom: 1
}

#bo_v_bot:after {
    display: block;
    visibility: hidden;
    clear: both;
    content: ""
}

#bo_v_bot h2 {
    position: absolute;
    font-size: 0;
    line-height: 0;
    overflow: hidden
}

#bo_v_bot ul {
    margin: 0;
    padding: 0;
    list-style: none
}

.bo_v_nb {
    float: left
}

.bo_v_nb li {
    float: left;
    margin-right: 1px
}

.bo_v_com {
    float: right
}

.bo_v_com li {
    float: left;
    margin-left: 1px
}


/* 게시판 뷰 본문 */

#bo_v_atc {
    padding: 20px 0px;
    min-height: 200px
}

#bo_v_atc_title {
    position: absolute;
    font-size: 0;
    line-height: 0;
    overflow: hidden
}

#bo_v_img {
    margin: 0 0 10px;
    width: 100%;
    overflow: hidden;
    zoom: 1
}

#bo_v_img:after {
    display: block;
    visibility: hidden;
    clear: both;
    content: ""
}

#bo_v_img img {
    max-width: 100%;
    height: auto
}

.editor_img_wrap {
    margin-bottom: 10px;
    text-align: center !important;
    display: block;
}

#bo_v_con {
    margin-bottom: 50px;
    width: 100%;
    line-height: 1.7em;
    word-break: break-all;
    overflow: hidden;
    font-size: 12px;
}

#bo_v_con a {
    color: #000;
    text-decoration: underline
}

#bo_v_con img {
    max-width: 100%;
    height: auto
}

#bo_v_con p {
    margin: 0;
}

#bo_v_act {
    margin-bottom: 30px;
    text-align: center;
}

#bo_v_act .bo_v_act_gng {
    position: relative
}

#bo_v_act a {
    margin-right: 5px;
    vertical-align: middle
}

#bo_v_act strong {
    color: #ff3061
}

#bo_v_act_good,
#bo_v_act_nogood {
    display: none;
    position: absolute;
    top: 30px;
    left: 0;
    padding: 10px 0;
    width: 165px;
    background: #ff3061;
    color: #fff;
    text-align: center;
}


/* 게시판 뷰 SNS 리스트 */

#bo_v_sns {
    margin: 0;
    padding: 0;
    list-style: none;
    zoom: 1;
    display: inline-block;
    vertical-align: middle;
    margin-right: 5px;
}

#bo_v_sns:after {
    display: block;
    visibility: hidden;
    clear: both;
    content: ""
}

#bo_v_sns li {
    display: inline-block;
    margin: 0 2px 0 0
}

.fz_middle_btn {
    *zoom: 1;
    margin-bottom: 10px;
}

.fz_middle_btn:after {
    content: "";
    display: block;
    clear: both;
}


/* 네임카드 */

.namecard {
    margin: 0 10px 40px;
    border: 1px solid #ddd;
    background: #fbfbfb;
}

.namecard .ncard_head {
    padding: 18px 19px 17px 19px;
    padding-top: 16px\9;
    line-height: normal;
    *zoom: 1;
}

.namecard .ncard_head:after {
    content: "";
    display: block;
    clear: both;
}

.namecard .ncard_head .ico_level {
    float: left;
    margin-right: 5px;
}

.namecard .ncard_head span {
    float: left;
    line-height: 15px;
    line-height: 14px\9;
    color: #777;
    font-size: 11px;
}

.namecard .ncard_head span em {
    color: #ff4200;
    font-style: normal;
    font-weight: bold;
    font-size: 11px;
}

.namecard .ncard_head span.txt_name {
    padding-top: 1px;
    padding-top: 2px\9;
    color: #000;
    font-size: 12px;
}

.namecard .ncard_head span.txt_name a {
    float: left;
    cursor: pointer;
}

.namecard .ncard_head span.txt_name span {
    margin-right: 5px;
    color: #000;
    font-weight: bold;
    font-size: 12px;
}

.namecard .ncard_head span.ico_dot {
    width: 23px;
    /* background: url('./img/sprites_ico.gif') no-repeat 10px -755px; */
    text-indent: -9000px;
}

.namecard .ncard_head span.txt_point {
    padding-top: 1px;
    padding-top: 2px\9;
}

.namecard .ncard_head a {
    font-size: 11px;
    color: #666;
}

.namecard .ncard_head a.btn_link {
    float: right;
    padding-left: 18px;
    /* background: url('./img/sprites_ico.gif') no-repeat 3px -783px; */
    cursor: pointer;
}

.namecard .ncard_head a.btn_link:hover {
    background-position: 3px -803px;
    text-decoration: none;
}

.namecard .ncard_head a span {
    line-height: normal;
    font-weight: bold;
}

.namecard .ncard_body {
    padding: 0 0 20px 19px;
    *zoom: 1;
}

.namecard .ncard_body:after {
    content: "";
    display: block;
    clear: both;
}

.namecard .ncard_body .thumb {
    float: left;
    margin-right: 15px;
    width: 60px;
    height: 60px;
    /* background: url('./img/sprites.gif') no-repeat 0 -168px; */
}

.namecard .ncard_body .txt_introduce {
    float: left;
    font-size: 12px;
    line-height: 16px;
}


/* 게시판 댓글 */

#bo_vc {
    margin: 0;
    font-size: 12px;
}

#bo_vc .bo_vc_top {
    border-top: 2px solid #bdbdbd;
    background: #f9f9f9;
    letter-spacing: -1px;
    color: #777;
    padding: 12px 0px;
}

#bo_vc .bo_vc_top h2 {
    /* background: url('./img/sprites_ico.gif') no-repeat 0 -707px; */
    padding: 1px 20px;
    font-size: 12px;
    margin: 0 0 0 20px !important;
}

#bo_vc .bo_vc_top em {
    font-style: normal;
    font-weight: 700;
    color: #ff4200;
    font-size: 11px;
}

#bo_vc article {
    padding: 10px;
    border-top: 1px solid #e3e3e3;
}

#bo_vc header {
    position: relative;
}

#bo_vc header .icon_reply {
    position: absolute;
    top: 15px;
    left: -20px
}

#bo_vc .sv_wrap {
    margin-right: 15px
}

#bo_vc .member,
#bo_vc .guest,
#bo_vc .sv_member,
#bo_vc .sv_guest {
    font-weight: 700
}

.bo_vc_hdinfo {
    display: inline-block;
    color: #aaa;
    /* font-family: tahoma; */
    font-size: 10px;
    position: relative;
    top: 1px
}

#bo_vc h1 {
    position: absolute;
    font-size: 0;
    line-height: 0;
    overflow: hidden
}

#bo_vc a {
    color: #000;
    text-decoration: none
}

#bo_vc p {
    padding: 0 0 5px;
    line-height: 1.8em
}

#bo_vc p a {
    text-decoration: underline
}

#bo_vc p a.s_cmt {
    text-decoration: none
}

#bo_vc #bo_vc_winfo {
    float: left
}

#bo_vc footer {
    zoom: 1;
}

#bo_vc footer:after {
    display: block;
    visibility: hidden;
    clear: both;
    content: ""
}

#bo_vc_empty {
    margin: 0;
    padding: 40px 0 40px !important;
    text-align: center;
    border-top: 1px solid #e3e3e3;
    font-size: 12px;
}

#bo_vc .bo_vc_content {
    padding: 10px 0;
}

#bo_vc .bo_vc_content img {
    vertical-align: middle;
}

#bo_vc .bo_vc_secret {
    display: inline-block;
    width: 19px;
    height: 14px;
    font-size: 11px;
    /* background: url('./img/sprites_ico.gif') no-repeat; */
    text-indent: -9000px;
    overflow: hidden;
    vertical-align: top;
    background-position: 0 -1008px;
}

#bo_vc article.ico_ind1 {
    padding-left: 40px;
}

#bo_vc article.ico_ind2 {
    padding-left: 80px;
}

#bo_vc article.ico_ind3 {
    padding-left: 120px;
}

#bo_vc article.ico_ind4 {
    padding-left: 160px;
}

#bo_vc article.ico_ind5 {
    padding-left: 200px;
}

#bo_vc article .indent {
    position: relative;
    float: left;
    width: 0;
    font-size: 0;
}

#bo_vc article .indent span {
    position: absolute;
    top: 2px;
    left: -22px;
    width: 12px;
    height: 11px;
    text-indent: -9000px;
    /* background: url('./img/sprites_ico.gif') no-repeat 0 -422px; */
}


/* 댓글 버튼 부분 */

.bo_vc_act {
    float: left;
    margin: 0;
    list-style: none;
    *zoom: 1;
    padding: 0;
}

.bo_vc_act:after {
    display: block;
    visibility: hidden;
    clear: both;
    content: ""
}

.bo_vc_act .list_btn {
    display: inline-block;
    margin-top: 17px;
    width: 13px;
    height: 14px;
    /* background: url('./img/sprites_ico.gif') no-repeat; */
    text-indent: -9000px;
    overflow: hidden;
}

.bo_vc_act .list_btn.btn_reply {
    margin-right: 12px;
    padding-left: 12px;
    width: auto;
    font-size: 11px;
    letter-spacing: -1px;
    text-indent: 0;
    background-position: 0 -442px;
}

.bo_vc_act .list_btn.btn_reply:hover,
.bo_vc_act .list_btn.btn_reply.cover {
    color: #666 !important;
    background-position: 0 -460px;
}

.bo_vc_act .list_btn.btn_edit {
    margin-right: 5px;
    background-position: 2px -558px;
}

.bo_vc_act .list_btn.btn_edit:hover,
.bo_vc_act .list_btn.btn_edit.cuover {
    background-position: 2px -577px;
}

.bo_vc_act .list_btn.btn_del {
    background-position: 3px -652px;
}

.bo_vc_act .list_btn.btn_del:hover {
    background-position: 3px -669px;
}

#bo_vc_w {
    position: relative;
    margin-bottom: 10px;
}

#bo_vc_w h2 {
    position: absolute;
    font-size: 0;
    line-height: 0;
    overflow: hidden
}

#bo_vc_w #char_cnt {
    display: block;
    margin: 0 0 5px
}

#bo_vc_sns {
    margin: 0;
    padding: 0;
    list-style: none;
    zoom: 1
}

#bo_vc_sns:after {
    display: block;
    visibility: hidden;
    clear: both;
    content: ""
}

#bo_vc_sns li {
    float: left;
    margin: 0 20px 0 0
}

#bo_vc_sns input {
    margin: 0 0 0 5px
}


/* 댓글 폼 부분 */

.comment_form .write_body {
    padding: 9px;
    border: 1px solid #e3e3e3;
    background: #fafafa;
}

.comment_form .write_body .write_mbinfo {
    padding: 0 0 5px 0;
    border: none;
}

.comment_form .btn_area .fleft {
    padding-top: 8px;
}

.comment_form .checkbox {
    width: 60px;
}

.comment_form .checkbox label {
    font-size: 11px;
    color: #777;
}

.comment_form .checkbox label span {
    height: 13px;
}

.no_editor {
    position: relative;
    *zoom: 1;
    margin-bottom: 10px;
}

.no_editor:after {
    clear: both;
    display: block;
    content: "";
}

.no_editor .bo_vc_wr_content {
    width: 100%;
    border: 1px solid #ccc;
    height: 100px !important;
    padding: 8px;
    line-height: 22px;
    font-size: 12px;
}

.write_body .btn_area {
    padding-top: 5px;
    *zoom: 1;
}

.write_body .btn_area:after {
    clear: both;
    display: block;
    content: "";
}

.write_body .btn_area input {
    float: right;
}

.comment_form .txt_limit span,
.write_option .txt_limit span {
    font-size: 11px;
    color: #777;
}

.comment_form .txt_limit span.txt_min {
    padding: 0 10px;
    border-right: 1px solid #dcdcdc;
}

.write_option .txt_limit span.txt_min {
    padding-right: 10px;
    border-right: 1px solid #dcdcdc;
}

.comment_form .txt_limit span.txt_max,
.write_option .txt_limit span.txt_max {
    padding-left: 10px;
    padding-right: 10px;
}

/* .comment_form .write_body {} */

.comment_form .write_body .fl {
    float: none;
    margin-bottom: 5px;
}

.comment_form .write_body .fr {
    float: none;
}

.comment_form .write_body .bo_vc_form {
    margin-bottom: 10px;
}

.comment_form .write_body .bo_vc_form .bo_vc_write_notice {
    height: 34px;
    line-height: 20px;
    padding-bottom: 6px;
    padding-left: 5px;
    font-weight: 700;
    color: #787878;
    font-size: 12px;
}

.comment_form .write_body .i_text {
    background: #fff;
    height: 28px;
    line-height: 28px;
    width: 100%;
    margin-bottom: 5px;
}

.comment_form .write_body #captcha_key {
    background: #fff !important;
}

.comment_form .write_body #captcha #captcha_info {
    display: none;
}

.comment_form .write_body .wr_secret_label {
    font-size: 11px;
}


/* 스코어 */

.article_score {
    padding: 50px 0px;
    text-align: center;
    *zoom: 1;
}

.article_score:after {
    content: "";
    display: block;
    clear: both;
}

.article_score a {
    display: inline-block;
    width: 33px;
    height: 23px;
    /* background: url('./img/sprites_btn.gif') no-repeat; */
    *zoom: 1;
    vertical-align: top;
}

.article_score a.btn_good {
    background-position: -270px 0;
}

.article_score a.btn_nogood {
    background-position: -308px 0;
}

.article_score .graph {
    display: inline-block;
    *zoom: 1;
    margin: 0 3px;
    padding: 5px 0;
    width: 215px;
    height: 23px;
    /* background: url('./img/sprites_btn.gif') no-repeat 0 -27px; */
}

.article_score .score_bar_wrap {
    display: block;
    float: left;
    width: 144px;
}

.article_score .graph .line_border {
    display: block;
    width: 3px;
    height: 10px;
    font-size: 0;
    /* background: url('./img/sprites_ico.gif') no-repeat 0 -1162px; */
    position: absolute;
    right: -3px;
}

.article_score .graph .score_bar {
    display: block;
    float: none;
    height: 10px;
    font-size: 0;
    /* background: url('./img/bg_recom1.gif') repeat-x 0 3px; */
    position: relative;
}

.article_score em {
    float: left;
    width: 34px;
    line-height: 13px;
    /* font-family: tahoma; */
    font-size: 9px;
    font-style: normal;
    text-align: center;
    font-weight: bold;
}

.article_score em.txt_good {
    color: #00529f;
}

.article_score em.txt_nogood {
    float: right;
    color: #9f0010;
}

.limit_score a {
    float: right;
}

.limit_score em {
    float: right;
    padding: 0 10px 0 0;
    width: auto;
    line-height: 23px;
}

@media (min-width:768px) {
    #bo_v_info .fl {
        padding-left: 15px;
    }
    #bo_v_info .fr {
        padding-right: 15px;
    }
    #bo_v_info .bo_v_user em {
        padding-left: 5px;
    }
    #bo_v_info .bar {
        margin: 0 10px;
    }
    .namecard {
        margin: 0 20px 40px 20px;
    }
    #bo_vc article {
        padding: 20px;
    }
    #bo_vc article.ico_ind1 {
        padding-left: 65px;
    }
    #bo_vc article.ico_ind2 {
        padding-left: 110px;
    }
    #bo_vc article.ico_ind3 {
        padding-left: 155px;
    }
    #bo_vc article.ico_ind4 {
        padding-left: 200px;
    }
    #bo_vc article.ico_ind5 {
        padding-left: 245px;
    }
    .comment_form .write_body .fl {
        float: left;
        margin-bottom: 0px;
    }
    .comment_form .write_body .fr {
        float: right;
    }
    .comment_form .write_body .i_text {
        width: auto;
        margin-bottom: 0px;
    }
    #bo_v_atc {
        min-height: 200px;
        height: auto !important;
        height: 200px;
        padding: 20px 20px 50px;
    }
    .article_score {
        padding: 50px 20px;
        text-align: center;
        *zoom: 1;
    }
    .article_score .graph {
        display: inline-block;
        *zoom: 1;
        margin: 0 3px;
        padding: 5px 0;
        width: 265px;
        height: 23px;
        /* background: url('./img/sprites_btn.gif') no-repeat 0 0; */
    }
    .article_score .score_bar_wrap {
        display: block;
        float: left;
        width: 194px;
    }
}


/* 글쓰기 상단 부분 */

.bo_write_option {
    padding: 0 10px;
    border: 1px solid #e3e3e3;
    border-bottom: none;
    background: #fafafa;
    *zoom: 1;
    height: 35px;
    line-height: 35px;
}

.bo_write_mbinfo {
    padding: 5px 10px 10px;
    border: 1px solid #e3e3e3;
    border-bottom: none;
    *zoom: 1;
    position: relative;
}

.bo_write_mbinfo:after {
    content: "";
    display: block;
    clear: both;
}

.bo_write_mbinfo ul {
    list-style: none;
    padding: 0;
    margin: 0;
    *zoom: 1;
    margin-right: 290px;
}

.bo_write_mbinfo ul:after {
    content: "";
    display: block;
    clear: both;
}

.bo_write_mbinfo ul li {
    width: 50%;
    float: left;
}

.bo_write_mbinfo .placeholder {
    margin-top: 5px;
    display: block;
    width: 98%;
}

.bo_write_mbinfo .i_text {
    height: 26px;
    line-height: 26px;
    display: block;
    width: 95%;
}

/* .bo_editor_wrap {} */

.bo_editor_wrap .placeholder {
    display: block;
}

.bo_w_form #captcha {
    position: absolute;
    right: 10px;
    top: 10px;
}

.bo_w_title {
    position: relative;
    margin-bottom: 10px;
    *zoom: 1;
}

.bo_w_title:after {
    content: "";
    display: block;
    clear: both;
}

.bo_w_title .placeholder {
    display: block;
}

.bo_w_title #wr_subject {
    display: block;
    width: 95%;
}


/* 글쓰기 파일, 링크 영역 */

.ad_sector {
    border: 1px solid #e3e3e3;
    margin-top: 10px;
}


/* 파일, 링크 버튼 */

.ad_sector .ad_btn_area {
    padding: 0;
    background: #fafafa;
    *zoom: 1;
}

.ad_sector .ad_btn_area:after {
    clear: both;
    display: block;
    content: "";
}

.ad_sector .ad_btn_area a {
    position: relative;
    float: left;
    padding: 10px 11px 9px 10px;
    border-right: 1px solid #e3e3e3;
    font-size: 11px;
    letter-spacing: -1px;
    z-index: 10;
}

.ad_sector .ad_btn_area a span {
    float: left;
    padding: 0 5px 0 16px;
    color: #666;
    /* background: url('./img/sprites_ico.gif') no-repeat; */
    font-size: 11px;
}

.ad_sector .ad_btn_area a.btn_file span {
    background-position: 0 -140px;
}

.ad_sector .ad_btn_area a.btn_link span {
    background-position: 0 -230px;
}

.ad_sector .ad_btn_area a.btn_file span.on {
    background-position: 0 -162px;
}

.ad_sector .ad_btn_area a.btn_link span.on {
    background-position: 0 -252px;
}

.ad_sector .ad_btn_area a.on {
    margin-bottom: -1px;
    border-bottom: 1px solid #ebebeb;
    background: #f2f2f2;
}

.ad_sector .ad_btn_area a em {
    float: left;
    font-weight: 700;
    font-style: normal;
    color: #ff4200;
}

.ad_sector .ad_btn_area .btn_wrap {
    float: right;
    padding: 8px 10px 0 0;
}

.ad_sector .ad_btn_area .btn_wrap a {
    padding: 0;
    width: 14px;
    height: 15px;
    border: none;
    /* background: url('./img/sprites_ico.gif') no-repeat; */
    overflow: hidden;
    text-indent: -9000px;
}

.ad_sector .ad_btn_area .btn_wrap a.btn_close {
    background-position: 3px -402px;
    display: none;
}


/* 파일, 링크 폼 */

.ad_sector .ad_form_area {
    clear: both;
    display: none;
    margin: 0;
    padding: 20px;
    border: none;
    border-top: 1px solid #e3e3e3;
    background: #f2f2f2;
}

.ad_sector .ad_form_area legend {
    position: absolute;
    top: 0;
    left: 0;
    width: 0;
    height: 0;
    overflow: hidden;
    visibility: hidden;
    font-size: 0;
    line-height: 0;
}

/* .ad_sector .ad_form_area input {} */

.ad_sector .ad_form_area ul {
    margin: 0;
    padding: 0;
    list-style: none;
    *zoom: 1;
}

.ad_sector .ad_form_area ul:after {
    clear: both;
    display: block;
    content: "";
}

.ad_sector .ad_form_area li {
    position: relative;
    padding-top: 10px;
    *zoom: 1;
}

.ad_sector .ad_form_area li:after {
    clear: both;
    display: block;
    content: "";
}

.ad_sector .ad_form_area li.first {
    margin: 0;
    padding: 0;
    background: none;
}

.ad_sector .ad_form_area li label {
    position: absolute;
    bottom: 5px;
    left: 5px;
    font-size: 14px;
    
}

.ad_sector .ad_form_area .inputbox {
    float: none;
}

.ad_sector .ad_form_area .filebox {
    float: left;
    margin-right: 1px;
    padding: 5px 3px 2px 5px !important;
    height: 15px;
    line-height: 15px;
    font-size: 12px;
    border: 1px solid #d2d2d2;
    background: #fff;
    width: 80%;
}

.ad_sector .ad_form_area .file_search {
    position: relative;
    display: inline;
    /* float: left;
    width: 58px;
    height: 24px; */
    /* background: url('./img/sprites_btn.gif') no-repeat -1600px 0; */
    overflow: hidden;
}

.ad_sector .ad_form_area .file_search .btn_file {
    position: absolute;
    top: 0;
    right: 1px;
    width: 100%;
    height: 100%;
    opacity: 0;
    filter: alpha(opacity=0);
}

.ad_sector .ad_form_area .bf_content_input {
    margin-top: 5px;
}

.ad_sector .ad_form_area .bf_content_input .i_text {
    background: #fff;
}

.file_btm {
    padding-top: 10px;
}

.file_btm .btn_file_add {
    float: left;
    width: 19px;
    height: 19px;
    /* background: url('./img/sprites_btn.gif') no-repeat -1119px 0; */
    text-indent: -9000px;
    overflow: hidden;
}

.file_btm .btn_file_del {
    float: left;
    width: 19px;
    height: 19px;
    margin-left: 1px;
    /* background: url('./img/sprites_btn.gif') no-repeat -1143px 0; */
    text-indent: -9000px;
    overflow: hidden;
}

.file_btm .txt_file_add {
    float: left;
    margin: 0;
    height: 19px;
    line-height: 21px;
    line-height: 23px\9;
    font-size: 11px;
    letter-spacing: -1px;
}

.file_btm .txt_file_add strong {
    color: #ff4200;
}


/* 파일, 링크 리스트 */

.ad_sector .ad_list_area {
    border-bottom: none;
}

.ad_sector .ad_list_area .first {
    border-top: 1px solid #e3e3e3;
}

.ad_sector .ad_list_area li {
    border-top: 1px solid #efefef;
}

.ad_sector .ad_list_area li span.txt_name,
.ad_sector .ad_list_area li span.txt_link,
.ad_sector .ad_list_area li span.txt_name {
    color: #666;
    background-position: 0 -94px;
}

.ad_sector .ad_list_area li span.txt_link {
    color: #0066cc;
    background-position: 0 -184px;
}

.ad_sector .ad_list_area li a.btn_del {
    float: right;
    padding: 0;
    width: 14px;
    background-position: 3px -651px;
    text-indent: -9000px;
    overflow: hidden;
}

.ad_sector .ad_list_area li a.btn_del:hover {
    background-position: 3px -668px;
}

#ad-form_link .i_text_large {
    background: #fff;
}

.cke_sc {
    display: none;
}


/* 에디터 단축키 감추기 */


/* 글쓰기 하단 버튼 부분 */

.write_foot {
    padding-top: 20px;
    text-align: center;
}

.write_foot a.btn_list {
    display: inline-block;
    width: 51px;
    height: 33px;
    /* background: url('./img/sprites_btn.gif') no-repeat -1663px 0; */
    text-indent: -9000px;
    overflow: hidden;
    vertical-align: top;
}


/* SELECT BOX */

.select-box {
    display: inline-block;
    position: relative;
    border: 1px solid #ccc;
    vertical-align: middle;
}

.select-box select {
    position: absolute;
    left: -9999px;
    display: none;
}

.select-box a {
    display: block;
    height: 25px;
    line-height: 25px;
    padding: 0 10px;
    /* background: #FFF url(./img/select_box_ico.png) right center no-repeat; */
    text-align: left !important;
    color: #333;
    font-size: 12px;
}

.select-box a:visited,
.select-box a:active,
.select-box a:link,
.select-box a:hover {
    text-decoration: none;
}

.select-box a i.fa {
    vertical-align: middle;
    float: right;
    padding-top: 7px;
}

.select-box ul {
    position: absolute;
    left: -1px;
    top: 25px;
    border: 1px solid #ccc;
    display: none;
    background: #fff;
    max-height: 240px;
    list-style: none;
    padding: 0;
    margin: 0;
    overflow: hidden;
    overflow-y: auto;
    z-index: 999;
    scrollbar-face-color: #eaeaea;
    scrollbar-track-color: #f7f7f7;
    scrollbar-3dlight-color: #e2e2e2;
    scrollbar-arrow-color: #8c8c8c;
    scrollbar-shadow-color: #eaeaea;
    scrollbar-highlight-color: #eaeaea;
    scrollbar-darkshadow-color: #e2e2e2;
}

.select-box ul a {
    display: block;
    line-height: 25px;
    height: 25px;
    background: none;
}

.select-box.select-active a {
    color: #4B87C8;
    /* background: #FFF url(./img/select_box_ico_active.png) right center no-repeat; */
}

.select-box.select-active a i {
    color: #4B87C8
}

.select-box.select-active ul a {
    color: #333;
    background: none;
}

.select-box.select-active ul a.hover {
    background: #8DA5C2;
    color: #fff
}

.select-box.select-active ul a.active {
    background: #4B7AB2;
    color: #fff
}


/* CHECK BOX */

.check-box {
    position: relative;
}

.check-box input {
    position: absolute;
    left: -9999px;
}

.check-box label {
    font-size: 12px;
    /* background: url(./img/checkbox.png) 0 center no-repeat; */
    display: inline-block;
    padding: 0 0 0 20px;
    line-height: 16px;
    height: 16px;
    cursor: pointer;
    margin: 0 10px 0 0;
    font-weight: normal;
}

/* .check-box label.checkbox-active {
    background: url(./img/checkbox_active.png) 0 center no-repeat;
} */


/* PAGING */

.fz_wrap .pg_wrap {
    clear: both;
    padding: 20px 0;
    text-align: center;
    margin: 0;
    font-size: 12px;
}

.fz_wrap .pg_page,
.fz_wrap .pg_current {
    display: inline-block;
    padding: 0 10px;
    height: 32px;
    color: #333;
    letter-spacing: 0;
    line-height: 30px;
    vertical-align: middle;
    border: 1px solid #ddd;
    background: #fff;
}

.fz_wrap .pg a:hover {
    background: #e8e8e8;
}

.fz_wrap .pg a:focus,
.fz_wrap .pg a:hover {
    text-decoration: none
}

.fz_wrap .pg_page {
    text-decoration: none
}

.fz_wrap .pg_current {
    display: inline-block;
    margin: 0 0 0 0;
    background: #888;
    color: #fff;
    font-weight: normal;
    border: 1px solid #888;
}

.fz_wrap .pg .fa {
    font-size: 14px;
}

/* .fz_wrap .pg_start,
.fz_wrap .pg_prev {}

.fz_wrap .pg_end,
.fz_wrap .pg_next {} */


/* 글쓰기 폼 */

.fz-form-title {
    font-size: 13px;
    margin-bottom: 5px;
    font-weight: 700;
}

.fz-form-content {
    margin-bottom: 20px;
}

#bo_w textarea {
    border: 1px solid #e4eaec;
    background: #f7f7f7;
    vertical-align: middle;
    line-height: 1.8em;
    -webkit-appearance: none;
    padding: 8px;
    line-height: 20px;
    height: 100px;
    width: 100%;
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
}

#bo_w .frm_file {
    display: block;
    margin-bottom: 5px;
}

#bo_w .i_text {
    width: 100%;
    height: 28px;
    line-height: 28px;
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
}

#captcha #captcha_img {
    height: 62px;
}

#captcha #captcha_key {
    height: 62px;
}

#captcha.m_captcha #captcha_key {
    height: 31px !important;
}

.fz_wrap nav {
    display: block !important;
}
</style>