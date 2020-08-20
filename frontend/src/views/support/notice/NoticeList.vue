<template>
    <div class="list-area">
        <template v-if="list && list.length">
            <div class="tbl-header">
                <table
                    cellpadding="0"
                    cellspacing="0"
                    border="0"
                >
                    <thead>
                        <tr id="t_header">
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
                            <td id="tb_title">
                                <router-link
                                    :to="{path: '/support/notice/'+ item.nid}"
                                >
                                    {{ item.title }}
                                </router-link>
                            </td>
                            <td id="tb_writer">
                                <div class="d-flex w-100 justify-content-center">
                                    {{ item.writer }}
                                </div>
                            </td>
                            <td id="tb_date">
                                {{ item.writeDate }}
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </template>

        <template v-else>
            <td>
                로딩중입니다..
            </td>
        </template>
        
        <template v-if="list && list.length">
            <notice-list-paginate
                :current="page"
                :last="pageCount"
            />
        </template>

        <write-button
            v-if="nickname && nickname === 'admin'"
            class="button float-right"
            :handler="write"
            :text="'작성'"
        />
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import NoticeListPaginate from "@/components/NoticeListPaginate";
import WriteButton from "@/components/WriteButton";

export default {
    name: "NoticeList",
    components: {
        NoticeListPaginate,
        WriteButton,
    },
    data: () => ({
        page: 0,
        pageCount: 1,
        list: [],
    }),
    computed: {
        ...mapGetters([
            'language',
            'nickname',
        ]),
    },
    watch: {
        '$route'() {
            this.page = this.$route.query.page;
        },
        page() {
            this.fetchNotices(this.page);
        },
    },
    created() {
        this.page = Number.parseInt(this.$route.query.page);
    },
    methods: {
        write() {
            this.$router.push({
                name: 'NoticeWrite'
            });
        },
        async fetchNotices(page) {
            const response = await this.$axios({
                url: `support/notice`,
                method: "get",
                params: {
                    page: page || 1,
                }
            })
                .catch((error) => {
                });

            if (response) {
                if (response.status >= 200 && response.status < 300) {
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
                        e.writer = '관리자';
                        e.writeDate = formatDate(e.writeDate);
                        return e;
                    });
                }
            }
        }
    }
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
	text-align: left;
	/* width: 455px; */
}
#t_header :nth-child(2) {
    width: 150px;
}
#t_header :nth-child(3) {
    width: 80px;
}
#t_body {
    border-bottom: 1px solid #BEDAE5;;
}
#tb_title {
    /* width: 455px; */
    text-align: left;
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
        width: 60%;
    }
    #t_header :nth-child(2) {
        width: 40%;
    }
    #t_header :nth-child(3) {
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
