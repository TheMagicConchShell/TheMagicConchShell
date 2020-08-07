<template>
    <div class="list-area">
        <div class="temporary-placer" />
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
                        <td class="text-left">
                            <router-link :to="{path: '/support/notice/'+ item.nid}">
                                {{ item.title }}
                            </router-link>
                        </td>
                        <td>{{ item.writer }}</td>
                        <td>{{ item.write_time }}</td>
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
            class="button float-right"
            :handler="write"
            :text="'작성'"
        />
    </div>
</template>

<script>
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
            }).catch((r) => {
                console.log(r);
                console.log("catch notices");
            });

            if (response) {
                console.log(response);
                if (response.status >= 200 && response.status < 300) {
                    this.pageCount = response.data.data.totalPages;
                    this.list = response.data.data.content;
                }
            }
        }
    }
};
</script>

<style scoped>
.temporary-placer {
    margin-top: 42px;
}

.list-area {
    width: 100%;
}

a {
    color: black;
}
a:hover {
    text-decoration: unset;
}
</style>
