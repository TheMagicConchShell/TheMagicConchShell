<template>
    <div v-cloak>
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
                    :to="{path: `/counsel/read/${item.postNo}`}"
                    class="flex-column align-items-start"
                    style="max-height: 210px;"
                >
                    <div class="d-flex w-100 justify-content-between">
                        <div class="item">
                            <span 
                                id="title"
                                :class="(item.selected)? 'selected' : ''"
                            >
                                {{ getSelected(item.selected) }}
                                <i
                                    v-if="item.selected"
                                    class="far fa-check-circle"
                                />
                            </span>
                        </div>
                        <div class="item">
                            <span>{{ getFormatDate(item.writeDate) }} </span>
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
                    <hr>
                    <div id="originPost">
                        {{ $t('account.post.origin') }}: {{ item.postTitle }}
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
            {{ $t('account.nocontent.reply')}}
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
        ...mapGetters([
            'nickname',
            'language',
        ]),
    },
    watch: {
        page() {
            this.fetchMyReplies(this.page);
        },
        language() {
            this.fetchMyReplies(this.page);
        },
    },
    created() {
        this.fetchMyReplies(this.page);
    },
    methods: {
        async fetchMyReplies(page) {
            const response = await this.$axios({
                method: 'get',
                url: `/counsel/reply/all/writer`,
                params: {
                    writer: this.nickname,
                    page: page || 1,
                },
            }).then((res) => {
                this.list = res.data.data.content;
                this.rows = res.data.data.totalElements;
                this.perPage = res.data.data.size;
            }).catch((error) => {
            });
        },
        getSelected(selected){
            if(selected) return this.$t('account.post.selected');
            else return this.$t('account.post.noselected');
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
#title {
    font-size: x-large;
}
#originPost {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: normal; 
    text-align: left;
    color: darkgray;
    margin-left: 1rem;
}
.item {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap; 
}
.content {
    width: 100%;
    height: 60px;
    margin: 1rem;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap; 
    text-align: left;
}
img {
    max-width: 100px;
    max-height: 100px;
}
.up {
    color: teal;
}
.down {
    color: tomato;
}
.selected {
    color: dodgerblue;
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
    white-space: nowrap;
}
</style>
