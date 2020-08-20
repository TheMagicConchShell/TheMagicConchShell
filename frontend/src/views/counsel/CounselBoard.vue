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
                            <th>{{ $t('board.views') }}</th>
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
                                    :to="{name: 'CounselDetail', params: {no: item.no}}"
                                >
                                    [{{ item.category }}] {{ item.title }} <small style="color: #5BBDCB">[{{ item.replies }}]</small>
                                </router-link>
                            </td>
                            <td id="tb_views">
                                {{ item.views }}
                            </td>
                            <td id="tb_writer">
                                <div class="d-flex w-100">
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
        fetchPostAll() {
            return this.$axios({
                url: 'counsel/post',
                methods: 'get',
                params: {
                    page: this.page,
                }
            });
        },
        fetchPostByCategory() {
            return this.$axios({
                url: 'counsel/post/category',
                methods: 'get',
                params: {
                    page: this.page,
                    category: this.category,
                }
            });
        },
        fetchPost(page) {
            this.$wait.start("board list load");
            this.$store.dispatch('fetchCategories');
            this.list = null;

            const fetching = this.category === '전체' ? this.fetchPostAll() : this.fetchPostByCategory();
            fetching
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
                            e.replies = 1;
                            e.category = this.$store.getters.categoryNameById(e.categoryId, this.language);
                            return e;
                        });
                        this.list.forEach((e) => {
                            this.fetchreplies(e.no)
                                .then((length) => {
                                    e.replies = length;
                                })
                                .catch(() => {});
                        });
                    }
                })
                .catch((error) => {
                    console.log(error);
                })
                .finally(() => {
                    this.$wait.end("board list load");
                });
        },
        fetchreplies(number) {
            return new Promise((resolve, reject) => {
                this.$axios({
                    url: '/counsel/post/post-no',
                    method: 'get',
                    params: {
                        no: number,
                    },
                })
                    .then((response) => {
                        if (response.status >= 200 && response.status < 300) {
                            resolve(response.data.data.replies.length);
                        } else {
                            reject(error);
                        }
                    })
                    .catch((error) => {
                        reject(error);
                    });
            });
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
    /* width: 455px; */
    text-align: left;
}
#t_header :nth-child(3) {
    width: 60px;
}
#t_header :nth-child(4) {
    width: 150px;
}
#t_header :nth-child(5) {
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
    text-align:left;
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
        display:none
    }
    #t_header :nth-child(4) {
        width: 40%;
    }
    #t_header :nth-child(5) {
        display:none
    }
    #tb_no {
        display:none;
    }
    #tb_title {
        width: 60%;
    }
    #tb_views {
        display:none
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