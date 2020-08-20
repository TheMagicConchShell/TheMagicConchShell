<template>
    <div>
        <template v-if="list && list.length">
            <table class="table">
                <colgroup>
                    <col width="10%">
                    <col width="30%">
                    <col width="15%">
                    <col width="15%">
                    <col width="15%">
                    <col width="15%">
                </colgroup>
                <thead class="thead-light">
                    <tr>
                        <th>
                            {{ $t('ranking.table.rank') }}
                        </th>
                        <th>
                            {{ $t('ranking.table.nickname') }}
                        </th>
                        <th
                            @click="changeSort(2)"
                        >
                            {{ $t('ranking.table.counsel') }}
                            <i 
                                v-if="mySort==2" 
                                class="fas fa-long-arrow-alt-down"
                            />
                            <i
                                v-else
                                class="fas fa-sort"
                            />
                        </th>
                        <th
                            @click="changeSort(3)"
                        >
                            {{ $t('ranking.table.reply') }}
                            <i 
                                v-if="mySort==3" 
                                class="fas fa-long-arrow-alt-down"
                            />
                            <i
                                v-else
                                class="fas fa-sort"
                            />
                        </th>
                        <th
                            @click="changeSort(4)"
                        >
                            {{ $t('ranking.table.point') }}
                            <i 
                                v-if="mySort==4" 
                                class="fas fa-long-arrow-alt-down"
                            />
                            <i
                                v-else
                                class="fas fa-sort"
                            />
                        </th>
                        <th
                            @click="changeSort(5)"
                        >
                            {{ $t('ranking.table.level') }}
                            <i 
                                v-if="mySort==5" 
                                class="fas fa-long-arrow-alt-down"
                            />
                            <i
                                v-else
                                class="fas fa-sort"
                            />
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr 
                        v-for="item in list"
                        :key="item.nickname"
                    >
                        <template v-if="item.rnum!=isMine">
                            <td>{{ item.rnum }}</td>
                            <td>{{ item.nickname }}</td>
                            <td>{{ item.postCount }}</td>
                            <td>{{ item.replyCount }}</td>
                            <td>{{ item.point }}</td>
                            <td>{{ item.level }}</td>
                        </template>
                        <template v-else>
                            <td 
                                class="my-rank"
                            >
                                {{ item.rnum }}
                            </td>
                            <td 
                                class="my-rank"
                            >
                                {{ item.nickname }}
                            </td>
                            <td 
                                class="my-rank"
                            >
                                {{ item.postCount }}
                            </td>
                            <td 
                                class="my-rank"
                            >
                                {{ item.replyCount }}
                            </td>
                            <td 
                                class="my-rank"
                            >
                                {{ item.point }}
                            </td>
                            <td 
                                class="my-rank"
                            >
                                {{ item.level }}
                            </td>
                        </template>
                    </tr>
                </tbody>
            </table>
        </template>
        <template v-if="myRank">
            <b-card
                title="내 순위"
            >
                <table class="table">
                    <colgroup>
                        <col width="10%">
                        <col width="30%">
                        <col width="15%">
                        <col width="15%">
                        <col width="15%">
                        <col width="15%">
                    </colgroup>
                    <tbody>
                        <tr
                            class="my-rank"
                        >
                            <td>{{ myRank.rnum }}</td>
                            <td>{{ myRank.nickname }}</td>
                            <td>{{ myRank.postCount }}</td>
                            <td>{{ myRank.replyCount }}</td>
                            <td>{{ myRank.point }}</td>
                            <td>{{ myRank.level }}</td>
                        </tr>
                    </tbody>
                </table>
            </b-card>
        </template>
    </div>
</template>

<script>
import { mapGetters } from 'vuex';
export default {
    data() {
        return {
            list:[],
            mySort:2,
            isMine:0,
            myRank:null
        };
    },
    computed: {
        ...mapGetters(['nickname']),
    },
    watch: {
        nickname(){
            this.$router.go(0);
        }
    },
    created() {
        this.$axios({
            url:"/rank/user",
            method:"GET",
            params:{
                sort:2
            }
        }).then((res)=>{
            if(res.data.status==="S-200"){
                res.data.data.forEach(element => {
                    if(this.nickname===element.nickname){
                        this.isMine=element.rnum;
                    }
                });;
                this.list = res.data.data;
                this.getMyRank(2);
            }
        }).catch(e=>{
            console.log(e);
        });
    },
    methods: {
        changeSort(val){
            this.isMine = 0;
            this.myRank = null;
            this.mySort = val;
            this.$axios({
                url:"/rank/user",
                method:"GET",
                params:{
                    sort:val
                }
            }).then((res)=>{
                if(res.data.status==="S-200"){
                    res.data.data.forEach(element => {
                        if(this.nickname===element.nickname){
                            this.isMine=element.rnum;
                        }
                    });;
                    this.list = res.data.data;
                    this.getMyRank(val);
                }
            }).catch(e=>{
                console.log(e);
            });
        },
        getMyRank(val){
            if(this.nickname&&this.isMine==0){
                this.$axios({
                    url:"/rank/user/mine",
                    method:"GET",
                    params:{
                        sort:val
                    }
                }).then((res)=>{
                    if(res.data.status==="S-200"){
                        console.log(res.data.data);
                        this.myRank = res.data.data;
                    }
                }).catch(e=>{
                    console.log(e);
                });
            }
        }
    },
};
</script>

<style>
.my-rank{
    font-weight: bold;
}
</style>