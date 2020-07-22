<template>
    <div>
        NOTICE
        <template v-if="list && list.length">
            <table
                v-for="item in list"
                :key="item.id"
            >
                <tr>
                    <td>{{ item.id }}</td>
                    <td>{{ item.title }}</td>
                    <td>{{ item.writer }}</td>
                </tr>
            </table>
        </template>
        <template v-else>
            <td>
                공지사항이 없습니다. {{ list && list.length }}
            </td>
        </template>
        <template v-if="list && list.length">
            <notice-list-paginate :last="pageCount" />
        </template>
    </div>
</template>

<script>
import NoticeListPaginate from "@/components/NoticeListPaginate";

export default {
    name: "NoticeList",
    components: {
        NoticeListPaginate
    },
    props: {
        page: {
            type: [Number, String],
            default: 1
        }
    },
    data: () => ({
        pageCount: 1,
        list: [{id:1}, {id:2}, {id:3}]
    }),
    async mounted() {
        await this.fetchNotices(this.page);
    },
    methods: {
        async fetchNotices(page) {
            const response = await this.$axios({
                url: `support/notice`,
                method: "get",
                params: {
                    page
                }
            }).catch(() => {});

            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.pageCount = 10;
                }
            }
        }
    }
};
</script>

<style scoped>
</style>
