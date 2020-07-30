<template>
    <paginate
        v-model="page"
        :page-count="last"
        :click-handler="pageHandle"
        :prev-text="'<'"
        :next-text="'>'"
        :hide-prev-next="true"
        :first-last-button="true"
        :container-class="'pagination'"
        :page-class="'page-item'"
        :active-class="'active'"
        :disabled-class="'disabled'"
    />
</template>

<script>
export default {
    name: 'CounselSelectMainPaginate',
    props: {
        current: {
            type: [Number],
            required: true,
        },
        last: {
            type: [Number],
            required: true,
        },
    },
    data: () => ({
        page: 1,
    }),
    mounted() {
        this.page = this.current;
    },
    methods: {
        pageHandle(nextPage) {
            this.$router.push({
                name: 'CounselSelectMain',
                query: {
                    page: nextPage,
                },
            }).catch(() => {});
        },
    },
};
</script>