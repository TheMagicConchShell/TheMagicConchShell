<template>
    <div>
        <v-wait for="board list load">
            <template slot="waiting">
                <infinite-loading />
            </template>
            <ul class="fz_list">
                <li class="fz_list_th">
                    <div class="fz_num">
                        번호
                    </div>
                    <div class="fz_subject">
                        제목
                    </div>
                    <div class="fz_writer">
                        작성자
                    </div>
                    <div class="fz_date">
                        날짜
                    </div>
                </li>
                <template v-if="!list">
                    <div
                        class=""
                    >
                        현재 등록된 문의가 없습니다
                    </div>
                </template>
                <li
                    v-for="item in list"
                    :key="item.no"
                >
                    <div class="fz_num hidden-xs">
                        {{ item.no }}
                    </div>
                    <div 
                        class="fz_subject"
                        style="text-align:center"
                    >
                        <router-link
                            :to="{name: 'QnaDetail', params: {no: item.no}}"
                        >
                            {{ item.title }}
                        </router-link>
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
            <QnaBoardPaginate
                :current="page"
                :last="pageCount"
            />
        </template>
        <template v-if="nickname">
            <div>
                <button
                    class="button-write btn btn-dark"
                    @click="write"
                >
                    문의하기
                </button>
            </div>
        </template>
    </div>
</template>

<script>
import QnaBoardPaginate from '@/components/qna/QnaBoardPaginate.vue';
import { mapGetters } from 'vuex';
export default {
    components: {
        QnaBoardPaginate
    },
    data: () => ({
        page:0,
        list: [],
        pageCount: 1,
    }),
    computed: {
        ...mapGetters(['nickname']),
    },
    watch: {
        '$route'() {
            this.page = this.$route.query.page;
        },
        page() {
            this.fetchQuestion(this.page);
        },
    },
    created() {
        this.page = Number.parseInt(this.$route.query.page);
    },
    methods: {
        fetchQuestion(page) {
            this.$wait.start("board list load"); 
            this.list = null;

            this.$axios({
                url: '/qna/question',
                method: 'GET',
                params: {
                    page: page || 1,
                }
            })
                .then((response) => {
                    console.log(response);
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
                			return e;
                		});
                	}
                })
                .catch((error) => {
                	console.log(error.response);
                })
                .finally(() => {
                	this.$wait.end("board list load");
                });
        },
        write() {
            this.$router.push({
                name: 'QnaRegist'
            });
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
	font-size: 12px;
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
	font-size: 11px;
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
	font-size: 12px;
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
	font-size: 12px;
}

.fz_total_count strong {
	color: #0e8185;
	font-weight: 700;
}

.fz_list {
	list-style: none;
	padding: 0;
	margin: 0 0 10px 0;
	width: 100%;
	display: table;
	font-size: 12px;
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
	color: #989898;
	font-weight: bold;
	font-size: 11px !important;
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
	font-size: 12px;
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
	font-size: 10px;
	/* font-family: tahoma; */
	color: #999
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
</style>