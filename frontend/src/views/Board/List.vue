<template>
    <div>
        고민게시판
        <div class="d-flex">
            <div id="category">
                고민 카테고리
                <ul
                    v-for="item in categories"
                    :key="item.id"
                >
                    <li>
                        {{ item.name }}
                    </li>
                </ul>
                <div class="border h-25">
                    광고 배너
                </div>
            </div>
            <div id="article">
                <ul id="tab">
                    <li style="background-color:#6B799E">
                        실시간 인기
                    </li>
                    <template v-for="item in categories">
                        <li :key="item.id">
                            {{ item.name }}
                        </li>
                    </template>
                </ul>
                <div>
                    <counsel-board
                        :page="page"
                    />
                </div>
                <button
                    class="button-write"
                    @click="write"
                >
                    작성
                </button>
            </div>
            <div class="border m-3 w-25">
                광고 배너
            </div>
        </div>
    </div>
</template>

<script>
import { mapState } from 'vuex';
import CounselBoard from '@/views/counsel/CounselBoard.vue';

export default {
    name: 'List',
    components: {
        CounselBoard,
    },
    data: () => ({
        page: 0,
        pageCount: 1,
    }),
    computed: {
        categories() {
            return this.$store.getters.categories;
        },

    },
    created() {
        this.$store.dispatch('fetchCategories');
    },
    methods: {
        write() {
            this.$router.push({
                name: 'counselregist'
            });
        },
    },
};
</script>

<style scoped>
#category {
    float: left;
    width: 10%;
    max-height: 100vh;
    border: 1px solid;
}
#tab {
    display:flex;
    padding: 0;
    width: 100%;
}
#tab li{
    clear: both;
    min-width: 12.5%;
    padding: 0 20px;
    display: inline-block;
    border: 1px solid;
}
#article {
    margin-left: 20px;
}

.button-write {
    float: right;
}
</style>
