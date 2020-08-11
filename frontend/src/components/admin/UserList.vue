<template>
    <div>
        <template v-if="userList && userList.length">
            <b-list-group
                id="user-list"
                :per-page="perPage"
                :current-page="page"
            >  
                <b-list-group-item
                    v-for="item in userList"
                    :key="item.id"
                    class="flex-column algin-items-start"
                >
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="item">
                            {{ item.uid }}
                        </h5>
                        <h5 class="item">
                            {{ item.email }}
                        </h5>
                        <h5 class="item">
                            {{ item.nickname }}
                        </h5>
                        <h5 class="item">
                            {{ item.authDate }}
                        </h5>
                        <h5 class="item">
                            {{ item.point }}
                        </h5>
                        <h5 class="item">
                            {{ item.level }}
                        </h5>
                    </div>
                </b-list-group-item>
            </b-list-group> 

            <div class="overflow-auto">
                <b-pagination
                    id="userPaginate"
                    v-model="page"
                    pills
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
import {mapGetters} from 'vuex';
import moment from 'moment';
export default {
    data:()=>({
        userList:[],
        page:1,
        rows:1,
        perPage:10,
    }),
    computed: {
        ...mapGetters('nickname')
    },
    watch:{
        page(){
            
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
                    page:page || 1,
                },
            }).then((res)=>{
                //console.dir(res);
                this.userList = res.data.data.content;
                this.rows = res.data.data.content.totalElements;
                this.page = res.data.data.content.size;

            }).catch((error) =>{
                console.log(error.response);
            });
        },
        getFormatDate(date) {
            return moment(new Date(date)).format("YYYY.MM.DD hh:mm:ss");
        },
    },
};
</script>

<style>

</style>