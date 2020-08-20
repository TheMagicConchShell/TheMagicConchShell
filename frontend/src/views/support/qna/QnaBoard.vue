<template>
    <div>
        <v-wait for="board list load">
            <template slot="waiting">
                <infinite-loading />
            </template>
            <div class="tbl-header">
                <table
                    cellpadding="0"
                    cellspacing="0"
                    border="0"
                >
                    <thead>
                        <tr id="t_header">
                            <th>{{ $t('board.no') }}</th>
                            <th>{{ $t('board.title') }}</th>
                            <th>{{ $t('board.writer') }}</th>
                            <th>{{ $t('board.date') }}</th>
                        </tr>
                    </thead>
                </table>
            </div>
            <div class="tbl-content">
                <table
                    cellpadding="0"
                    cellspacing="0"
                    border="0"
                >
                    <tbody>
                        <tr 
                            v-for="item in list"
                            id="t_body"
                            :key="item.no"
                        >
                            <td id="tb_no">
                                {{ item.no }}
                            </td>
                            <td id="tb_title">
                                <router-link
                                    :to="{name: 'QnaDetail', params: {no: item.no}}"
                                >
                                    {{ item.title }}
                                    <span
                                        v-if="item.answers"
                                        class="badge badge-success"
                                    >                                    <i
                                        v-if="item.answers"
                                        class="fas fa-check"
                                    /> Answered</span>
                                </router-link>
                            </td>
                            <td id="tb_writer">
                                <div class="d-flex align-self-center">
                                    <img
                                        :src="item.profileImg? item.profileImg: require(`@/assets/images/default_profile.png`)"
                                        style="width: 20px;height: 20px; border-radius: 20px; margin-right: 5px"
                                    >{{ item.writer }}
                                </div>
                            </td>
                            <td id="tb_date">
                                {{ item.writeDate }}
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
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
                            e.answers = 0;
                            e.writeDate = formatDate(e.writeDate);
                			return e;
                        });
                        this.list.forEach((e) => {
                            this.fetchanswers(e.no)
                                .then((length) => {
                                    e.answers = length;
                                });
                        });
                        console.log(this.list);
                	}
                })
                .catch((error) => {
                    this.$toast('안내', '문의를 불러올 수 없습니다. 잠시 후 다시 시도해주세요.');
                    console.log(error.response);
                })
                .finally(() => {
                	this.$wait.end("board list load");
                });
        },
        fetchanswers(number) {
            return new Promise((resolve, reject) => {
                this.$axios({
                    url: '/qna/answer',
                    method: 'get',
                    params: {
                        qid: number,
                    },
                })
                    .then((response) => {
                        if (response.status >= 200 && response.status < 300) {
                            resolve(response.data.data.length);
                        } else {
                            resolve(0);
                        }
                    })
                    .catch((error) => {
                        resolve(0);
                    });
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
a:hover {
    text-decoration: none;
}
table{
  width:100%;
  table-layout: fixed;
}
.tbl-header{
  background-color: #A6C2CE;
  border-bottom: 1px solid #1F1762;
 }
.tbl-content{
  margin: 0 0 10px 0;
}
th{
  padding: 10px 15px;
  text-align: center;
  font-family: sb;
  font-size: 12px;
}
#t_header :nth-child(1) {
    width: 60px;
}
#t_header :nth-child(2) {
    text-align: left;
	/* width: 455px; */
}
#t_header :nth-child(3) {
    width: 150px;
    text-align: left;
    padding-left: 40px;
}
#t_header :nth-child(4) {
    width: 80px;
}
#t_body {
    border-bottom: 1px solid #BEDAE5;;
}
#tb_no {
    width: 60px;
}
#tb_title {
    /* width: 455px; */
    text-align: left;
}
#tb_views {
    width: 60px;
}
#tb_writer {
    width: 150px;
    overflow: hidden;
    word-break: break-all;
}
#tb_date {
    width: 80px;
}
@media (max-width: 992px) {
    #t_header :nth-child(1) {
        display:none
    }
    #t_header :nth-child(2) {
        width: 60%;
    }
    #t_header :nth-child(3) {
        width: 40%;
    }
    #t_header :nth-child(4) {
        display:none
    }
    #tb_no {
        display:none;
    }
    #tb_title {
        width: 60%;
    }
    #tb_writer {
        width: 40%;
    }
    #tb_date {
        display:none
    }
}

td{
  padding: 15px;
  text-align: center;
  vertical-align:middle;
  font-weight: 300;
  font-size: 12px;
}
#t_no {
    width: 10%;
}
#t_title {
    width: 60%;
}

/* demo styles */

@import url(https://fonts.googleapis.com/css?family=Roboto:400,500,300,700);
body{
  background: -webkit-linear-gradient(left, #25c481, #25b7c4);
  background: linear-gradient(to right, #25c481, #25b7c4);
  font-family: 'Roboto', sans-serif;
}
section{
  margin: 50px;
}

</style>