<template>
    <div class="list-area">
        <template v-if="list && list.length">
            <table
                class="table"
            >
                <colgroup>
                    <col width="10%">
                    <col width="60%">
                    <col width="15%">
                    <col width="15%">
                </colgroup>
                <thead class="thead-dark">
                    <tr>
                        <th
                            class="text-left"
                            scope="col"
                            colspan="2"
                        >
                            제목
                        </th>
                        <th scope="col">
                            작성자
                        </th>
                        <th scope="col">
                            작성시간
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr 
                        v-for="item in list"
                        :key="item.nid"
                    >
                        <td>{{ item.nid }}</td>
                        <td class="title text-left">
                            <router-link :to="{path: '/support/notice/'+ item.nid}">
                                {{ item.title }}
                            </router-link>
                        </td>
                        <td>{{ item.writer }}</td>
                        <td>{{ item.writeDate }}</td>
                    </tr>
                </tbody>
            </table>
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
.list-area {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
}

a {
    color: black;
}
a:hover {
    text-decoration: unset;
}

table {
    table-layout: fixed;
}

.title {
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden; 
}
</style>
