<template>
    <div>
        <template v-if="list && list.length">
            <b-list-group
                id="my-post-list"
                :per-page="perPage"
                :current-page="page"
            >
                <b-list-group-item
                    v-for="item in list"
                    :key="item.id"
                    router-link 
                    :to="{path: `/counsel/read/${item.no}`}"
                    class="flex-column align-items-start"
                    style="max-height: 200px;"
                >
                    <div class="d-flex w-100 justify-content-between">
                        <div class="item title">
                            <span>{{ item.title }}</span>
                        </div>
                        <div class="item extra-info">
                            <span>{{ getCategory(item.categoryId) }}</span>
                            <span id="divider">|</span>
                            <span>{{ getFormatDate(item.writeDate) }}</span>
                            <span id="divider">|</span>
                            <span>{{ item.views }}</span>
                            <span id="divider">|</span>
                            <span style="margin-right: 0.5rem">
                                <i class="far fa-thumbs-up up" />
                                {{ item.likeCount }}
                            </span>
                            <span>
                                <i class="far fa-thumbs-down down" />
                                {{ item.unlikeCount }}
                            </span>
                        </div>
                    </div>
                    <div class="d-flex w-100 justify-content-between">
                        <!-- eslint-disable -->
                        <viewer
                            class="item content"
                            v-html="removeImg(item.content)"
                        />
                        <!-- eslint-disable -->
                        <img
                            v-if="getImg(item.content)"
                            :src="getImg(item.content)"
                        >
                    </div>
                </b-list-group-item>
            </b-list-group>

            <div class="overflow-auto">
                <b-pagination
                    id="myPostPaginate"
                    v-model="page"
                    pills
                    :total-rows="rows"
                    :per-page="perPage"
                    first-text="First"
                    prev-text="Prev"
                    next-text="Next"
                    last-text="Last"
                    align="center"
                    aria-controls="my-post-list"
                />
            </div>
        </template>

        <template v-else>
            고민 글 작성 내역이 없습니다.
        </template>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import moment from 'moment';

export default {
    data: () => ({
        list: [],
        page: 1,
        rows: 1,
        perPage: 10,
    }),
    computed: {
        ...mapGetters(['nickname']),
    },
    watch: {
        page() {
            this.fetchMyPosts(this.page);
        },
    },
    created() {
        this.$store.dispatch('fetchCategories');
        this.fetchMyPosts(this.page);
    },
    methods: {
        async fetchMyPosts(page) {
            const response = await this.$axios({
                method: 'get',
                url: `/counsel/post/all/writer`,
                params: {
                    writer: this.nickname,
                    page: page || 1,
                },
            }).then((res) => {
                this.list = res.data.data.content;
                this.rows = res.data.data.totalElements;
                this.perPage = res.data.data.size;
            }).catch((error) => {
                console.log(error.response);
            });
        },
        getCategory(categoryId){
            return this.$store.getters.categoryNameById(categoryId);
        },
        getFormatDate(date) {
            return moment(new Date(date)).format("YYYY.MM.DD HH:mm:ss");
        },
        removeImg(html) {
            let img_tag = /<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>/gi;
            return html.replace(img_tag, "");
        },
        getImg(html) {
            let img_tag = /<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>/gi;
            return (img_tag.exec(html) || [''])[1];
        },
    },
};
</script>

<style scoped>
#myPostPaginate {
    margin-top: 1rem;
}
#divider {
    margin-left: 1rem;
    margin-right: 1rem;
    color: lightgray;
}
.title {
    max-width: 50%;
    font-size: x-large;
    text-align: left;
}
.extra-info {
    text-align: right;
}
.item {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    padding: 5px;
}
.content {
    width: 100%;
    height: 100px;
    margin: 1rem;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap; 
    text-align: left;
}
.item:nth-child(1) { flex: 1 1 100px; }
.item:nth-child(2) { flex: 1 1 100px; }
img {
    max-width: 120px;
    max-height: 120px;
}
.up {
    color: teal;
}
.down {
    color: tomato;
}
.content >>> * {
    width: 100%;
    height: 100%;
    overflow: hidden;
    position: relative;
    display: inline-block;
    margin-right: 1rem;
    text-decoration: none;
    text-overflow: ellipsis;
    white-space: normal;
}
</style>
