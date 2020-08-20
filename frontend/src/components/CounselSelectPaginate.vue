<template>
    <div class="d-flex justify-content-center">
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
            :page-link-class="'page-link'"
            :prev-class="'page-item'"
            :prev-link-class="'page-link'"
            :next-class="'page-item'"
            :next-link-class="'page-link'"
            :active-class="'active'"
            :disabled-class="'disabled'"
        />
    </div>
</template>

<script>
export default {
    name: 'CounselSelectPaginate',
    props: {
        current: {
            type: [Number],
            required: true,
        },
        last: {
            type: [Number],
            required: true,
        },
        pageHandle:{
            type: Function,
            required: true,
        }
    },
    data: () => ({
        page: 1,
    }),
    mounted() {
        this.page = this.current;
    },
};
</script>
<style>
.page-link {
    position: relative;
    display: block;
    padding: .5rem .75rem;
    margin-left: -1px;
    line-height: 1.25;
    color: #007bff;
    background-color: #fff;
    border: 1px solid #dee2e6;
}
.page-item.active a {
    background-color: #0363BA !important;
}
.pagination{
    display: flex;
    padding-left: 0;
    list-style: none;
    list-style-position: initial;
    list-style-image: initial;
    list-style-type: none;
    border-radius: .25rem;
}
</style>