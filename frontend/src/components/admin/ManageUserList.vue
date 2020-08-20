<template>
    <div>
        <template v-if="userList && userList.length">
            <b-list-group
                id="user-list"
                :per-page="perPage"
                :current-page="page"
            >
                <b-list-group-item
                    class="flex-column algin-items-start"
                    variant="dark"
                >
                    <div class="d-flex w-100 justify-content-between">
                        <span class="head">
                            UID
                        </span>
                        <span class="head">
                            EMAIL
                        </span>
                        <span class="head">
                            NICKNAME
                        </span>
                        <span class="head">
                            AUTHDATE
                        </span>
                        <span class="head">
                            POINT
                        </span>
                        <span class="head">
                            LEVEL
                        </span>
                    </div>
                </b-list-group-item>
                <b-list-group-item
                    v-for="item in userList"
                    :key="item.id"
                    class="flex-column algin-items-start"
                >
                    <div class="d-flex w-100 justify-content-between">
                        <span class="item">
                            {{ item.uid }}
                        </span>
                        <span class="item">
                            {{ item.email }}
                        </span>
                        <span class="item">
                            {{ item.nickname }}
                        </span>
                        <span class="item">
                            {{ getFormatDate(item.authDate) }}
                        </span>
                        <span class="item">
                            {{ item.point }}
                        </span>
                        <span class="item">
                            {{ item.level }}
                        </span>
                    </div>
                </b-list-group-item>
            </b-list-group> 

            <div class="overflow-auto">
                <b-pagination
                    id="userPaginate"
                    v-model="page"
                    :total-rows="rows"
                    :per-page="perPage"
                    first-text="First"
                    prev-text="Prev"
                    next-text="Next"
                    last-text="Last"
                    align="center"
                    aria-controls="user-list"
                />
            </div>
        </template>

        <template v-else>
            유저 목록이 없습니다.
        </template>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
import moment from 'moment';
export default {
    data:()=>({
        userList:[],
        page:1,
        rows:1,
        perPage:10,
    }),
    computed: {
        ...mapGetters(['nickname']),
    },
    watch:{
        page() {
            this.fetchUserList(this.page);
        },
    },
    created() {
        this.fetchUserList(this.page);
    },
    methods: {
        async fetchUserList(page){
            const response = await this.$axios({
                method: 'get',
                url: '/user/all',
                headers:{
                    nickname : this.nickname,
                },
                params:{
                    page: page || 1,
                },
            }).then((res)=>{
                this.userList = res.data.data.content;
                this.rows = res.data.data.totalElements;
                this.perPage = res.data.data.size;

            }).catch((error) =>{
            });
        },
        getFormatDate(date) {
            return moment(new Date(date)).format("YYYY.MM.DD HH:mm:ss");
        },
    },
};
</script>

<style scoped>
.item {
    flex: 1 1 100px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap; 
}
.head {
    flex: 1 1 100px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    font-weight: bold;
}
#userPaginate {
    margin-top: 1rem;
}
</style>