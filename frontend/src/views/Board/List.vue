<template>
    <div>
        <div id="home">
            <span v-if="language==='ko'">고민게시판</span>
            <span v-else>Counsel</span>
        </div>
        <div class="d-flex">
            <div id="article">
                <ul id="tab">
                    <li>전체</li>
                    <li
                        v-for="category in categories"
                        :key="category.id"
                    >
                        {{ category.name }}
                    </li>
                </ul>
                <div>
                    <counsel-board
                        :page="page"
                    />
                </div>
                <div />
                <button
                    class="button-write"
                    @click="write"
                >
                    작성
                </button>
            </div>
            <transition name="right-side">
                <div
                    v-show="Y"
                    id="category"
                >
                    고민 카테고리
                    <ul>
                        <li>
                            전체
                        </li>
                        <li
                            v-for="cg in categories"
                            :key="cg.no"
                        >
                            {{ cg.name }}
                        </li>
                    </ul>
                </div>
            </transition>
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
        page: 1,
        pageCount: 1,
        Y: false,
        nowcategory: null,
    }),
    computed: {
        categories() {
            return this.$store.getters.categories;
        },
        ...mapState(['language'])
    },
    created() {
        this.$store.dispatch('fetchCategories');
    },
    mounted() {
        this.$nextTick(window.addEventListener('scroll', this.detectWindowScrollY));
        // window.addEventListener('scroll', this.detectbottom);
    },
    methods: {
        write() {
            this.$router.push({
                name: 'counselregist'
            });
        },
        detectWindowScrollY() {
            this.Y = window.scrollY > 100;
        },
        // async detectbottom() {
        //     if (window.scrollTop === document.height) {
        //         this.page += 1;
        //     };
        // },
        async fetchCategories() {
            const response = await this.$axios({
                url: `counsel/category`,
                method: "get",
            }).catch(() => {
                console.log("catch notices");
            });
            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.categories = response.data.data.content;
                }
            }
        },
    }
};
</script>

<style scoped>
#home {
  display: flex;
  margin: 30px 0;
  justify-content: space-between;
  font-size: 30px;
}
#category {
    position: fixed;
    right: 10px;
    width: 10%;
    border: 1px solid;
    background-color: #ffffff;
}
#tab {
    display:flex;
    align-items: center;
    padding: 0;
    width: 100%;
    height: 24px;
    font-size: 16px;
    background-color: #ffffff;
}
#tab li{
    clear: both;
    min-width: 12.5%;
    display: inline-block;
    border: 1px solid;
    
}
#article {
    margin-left: 20px;
    width: 100%;
}

.button-write {
    float: right;
}
</style>
