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
                        v-for="(item,index) in list"
                        :key="item.nickname"
                    >
                        <td>{{ index+1 }}</td>
                        <td>{{ item.nickname }}</td>
                        <td>{{ item.postCount }}</td>
                        <td>{{ item.replyCount }}</td>
                        <td>{{ item.point }}</td>
                        <td>{{ item.level }}</td>
                    </tr>
                </tbody>
            </table>
        </template>
    </div>
</template>

<script>
export default {
    data() {
        return {
            list:[],
            mySort:2
        };
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
                this.list = res.data.data;
            }
        }).catch(e=>{
            console.log(e);
        });
    },
    methods: {
        changeSort(val){
            this.mySort = val;
            this.$axios({
                url:"/rank/user",
                method:"GET",
                params:{
                    sort:val
                }
            }).then((res)=>{
                if(res.data.status==="S-200"){
                    this.list = res.data.data;
                }
            }).catch(e=>{
                console.log(e);
            });
        },
        
    },
};
</script>

<style>

</style>