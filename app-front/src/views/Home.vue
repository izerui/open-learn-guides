<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <v-data-table
            :headers="headers"
            :items="workList"
            style="width:100%;height: 100%;"
            class="elevation-1">
        <template v-slot:items="props">
            <td>{{ props.item.ExerciseName }}</td>
            <td class="text-xs-right">
                <div>
                    {{props.item.StartDate}} 至 {{props.item.EndDate}}
                </div>
            </td>
            <td class="text-xs-right">
                <span>{{props.item.SubmitCount}} / {{props.item.MaxTimesOfTrying}} 次</span>
            </td>
            <td class="text-xs-right">{{ props.item.MaxScore }}</td>
            <td class="text-xs-right">
                <div>
                    <v-btn size="mini" v-if="props.item.state === '2' || props.item.state === '3'"
                           @click="gotoDoWork(props.item)">继续作答
                    </v-btn>
                    <v-btn size="mini" v-else-if="props.item.state === '4'">批阅中</v-btn>
                    <v-btn size="mini" v-else-if="props.item.state === '5'" @click="gotoDoWork(props.item)">重做</v-btn>
                    <v-btn size="mini" v-else @click="gotoDoWork(props.item)">做作业</v-btn>
                </div>
            </td>
        </template>

        <!-- 试卷详情页 -->
        <v-dialog
                v-model="dialogShow"
                fullscreen
                hide-overlay
                transition="dialog-bottom-transition"
                scrollable
        >
            <v-card tile>
                <v-toolbar card dark color="primary">
                    <v-btn icon dark @click="dialog = false">
                        <v-icon>close</v-icon>
                    </v-btn>
                    <v-toolbar-title>试卷名称</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-toolbar-items>
                        <v-btn dark flat @click="dialog = false">保存答案</v-btn>
                    </v-toolbar-items>
                </v-toolbar>
                <v-card-text>
                    <v-list three-line subheader>
                        <v-subheader>单选题</v-subheader>
                        <v-list-tile avatar>
                            <v-list-tile-content>
                                <v-list-tile-title>题目一</v-list-tile-title>
                                <v-list-tile-sub-title>A. 答案是冬季风</v-list-tile-sub-title>
                                <v-list-tile-sub-title>B. 答案是冬季风</v-list-tile-sub-title>
                                <v-list-tile-sub-title>C. 答案是冬季风</v-list-tile-sub-title>
                                <v-list-tile-sub-title>D. 答案是冬季风</v-list-tile-sub-title>
                            </v-list-tile-content>
                        </v-list-tile>
                        <v-list-tile avatar>
                            <v-list-tile-content>
                                <v-list-tile-title>Password</v-list-tile-title>
                                <v-list-tile-sub-title>Require password for purchase or use password to restrict
                                    purchase
                                </v-list-tile-sub-title>
                            </v-list-tile-content>
                        </v-list-tile>
                    </v-list>
                    <v-divider></v-divider>
                </v-card-text>
            </v-card>
        </v-dialog>
    </v-data-table>
</template>

<script>
    // @ is an alias to /src

    export default {
        name: 'home',
        components: {},
        data() {
            return {
                workList: [],
                paperShow: false,
                paperJson: {},
                headers: [
                    {
                        text: '作业名称',
                        align: 'left',
                        // sortable: false,
                        value: '',
                    },
                    {text: '起止时间', value: ''},
                    {text: '提交次数', value: ''},
                    {text: '最高分', value: ''},
                    {text: '操作', value: ''},
                ],
                dialogShow: false,
            };
        },
        created() {
            this.getMyWork();
        },
        methods: {
            async getMyWork() {
                const result = await this.$getUrl("/myWork");
                var alist = new Array();
                result.forEach(s => {
                    s.Data.forEach(d => {
                        d.Count = s.Count;
                        d.CourseName = s.CourseName;
                        d.url = s.url;
                        alist.push(d);
                    });
                })
                this.workList = alist;
                console.log("作业列表...", this.workList);
            },
            async gotoDoWork(row) {
                this.$message.info(JSON.stringify(row))
                this.dialogShow = true;
                // console.log("点击行", row);
                // const url = `http://learn.open.com.cn/StudentCenter/OnLineJob/Redirect?mode=1&courseExerciseID=${row.CourseExerciseID}&submitCount=${row.SubmitCount}&studentHomeworkId=${row.studentHomeworkId}`;
                // console.log('跳转试卷页面', url);
                // const key = await this.$get("/getTestPagerKey", {url: encodeURI(url)});
                // const testPaperUrl = `http://learn.open.com.cn/StudentCenter/OnlineJob/DoHomework?courseExerciseId=${row.CourseExerciseID}&key=${key}&studentHomeworkId=${row.studentHomeworkId}&_=${(new Date()).getTime()}`;
                // console.log("请求试卷key", testPaperUrl);
                // this.paperJson = await this.$get("/getTestPaper", {url: encodeURI(testPaperUrl)});
                // console.log("获取试卷", this.paperJson);
                // this.paperShow = true;
                // window.open(url,"_blank");
            }
        }
    }
</script>
