<template>
    <div>
        <template v-if="list && list.length">
            <table class="table">
                <thead class="thead-light">
                    <tr>
                        <th>
                            순위
                        </th>
                        <th>
                            닉네임
                        </th>
                        <th>
                            고민
                            <i 
                                v-if="mySort==2" 
                                class="fas fa-long-arrow-alt-down"
                            />
                        </th>
                        <th>
                            답변
                            <i 
                                v-if="mySort==3" 
                                class="fas fa-long-arrow-alt-down"
                            />
                        </th>
                        <th>
                            포인트
                            <i 
                                v-if="mySort==4" 
                                class="fas fa-long-arrow-alt-down"
                            />
                        </th>
                        <th>
                            레벨
                            <i 
                                v-if="mySort==5" 
                                class="fas fa-long-arrow-alt-down"
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
    props:{
        mySort:{
            type:Number,
            required:true
        }
    },
    data() {
        return {
            list:[]
        };
    },
    created() {
        this.$axios({
            url:"/rank/user",
            method:"GET",
            params:{
                sort:this.mySort
            }
        }).then((res)=>{
            if(res.data.status==="S-200"){
                this.list = res.data.data;
            }
        }).catch(e=>{
            console.log(e);
        });
    },

};
</script>

<style>

</style>