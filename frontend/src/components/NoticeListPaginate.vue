<template>
    <nav>
        <paginate
            :page-count="last"
            :click-handler="pageHandle"
            :prev-text="'Prev'"
            :next-text="'Next'"
            container-class="pagination"
            page-class="page-item"
        />

        <ul class="pagination">
            <li><a href="#">2</a></li>
            <li><a href="#">3</a></li>
            <li><a href="#">4</a></li>
            <li><a href="#">5</a></li>
        </ul>
    </nav>
</template>

<script>
export default {
    name: 'NoticeListPaginate',
    props: {
        current: {
            type: [Number, String],
            default: 1,
        },
        last: {
            type: [Number, String],
            default: 1,
        }
    },
    methods: {
        pageHandle(nextPage) {
            this.$emit('page-move', nextPage);
        },
    },
};
</script>

<style scoped>
</style>