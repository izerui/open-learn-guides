<template>
    <el-dialog :visible.sync="dialogShow" :title="titleLabel" :close-on-click-modal="false">
        <el-collapse>
            <el-collapse-item :title="sec.Title" :name="sec.Identifier"
                              v-for="sec in sections">
                <el-card v-for="(qes,index) in sec.items">
                    <div slot="header" class="clearfix">
                        <span>{{index+1}}. </span>
                        <span>{{qes.I2}}</span>
                        <span style="float: right;">{{index+1}}. {{qes.ans}}</span>
                    </div>
                    <div v-for="ch in qes.Choices" class="text item">
                        <span>{{ch.I1}}. </span>
                        {{ch.I2}}
                    </div>
                </el-card>
            </el-collapse-item>
        </el-collapse>

        <span slot="footer" class="dialog-footer">
            <span v-if="qesAnswers" style="float: left;">{{qesAnswers.length}}个答案</span>
            <el-button type="primary" v-if="doneAnswer" @click="saveHomeWork">保存答案</el-button>
            <el-button type="primary" v-else disabled="">保存答案</el-button>
        </span>
    </el-dialog>
</template>

<script>
    import jsonViewer from 'vue-json-viewer';

    export default {
        name: "TestPaper",
        components: {
            jsonViewer
        },
        props: {
            show: {
                type: Boolean,
                default: false
            },
            paperJson: {
                type: Object,
                required: false,
            }
        },
        data() {
            return {
                itemBankId: '',
                sections: [],
                doneAnswer: false,
                qesAnswers: [],
            }
        },
        computed: {
            dialogShow: {
                get() {
                    if (this.show) {
                        this.qesAnswers = [];
                        this.doneAnswer = false;
                        this.wrapSections();
                        this.getAnswers();
                    }
                    return this.show;
                },
                set(val) {
                    this.$emit('update:show', val);
                }
            },
            titleLabel() {
                if (this.doneAnswer) {
                    return this.paperJson.ExerciseName;
                } else {
                    return this.paperJson.ExerciseName + "   -  (正在获取答案)...";
                }
            }
        },
        methods: {
            wrapSections() {
                this.qesAnswers = [];
                this.itemBankId = this.paperJson.TestPaperContent.Model.P3;

                this.sections = this.paperJson.TestPaperContent.Sections;

                const questions = this.paperJson.TestPaperContent.Items;

                this.sections.forEach(s => {
                    if (!s.items) {
                        s.items = [];
                    }
                    s.ItemID.forEach(id => {
                        questions.forEach(ques => {
                            if (ques.I1 === id) {
                                s.items.push(ques);
                            }
                        })
                    })
                });
                console.log("包装后", this.sections);
            },
            async getAnswers() {
                for (var i = 0; i < this.sections.length; i++) {
                    const s = this.sections[i];
                    for (var j = 0; j < s.items.length; j++) {
                        const q = s.items[j];
                        const res = await this.$post("/getAnswer", {
                            itemBankId: this.itemBankId,
                            questionId: q.I1
                        });
                        res.ans = "答案: ";
                        res.Choices.forEach(ch => {
                            if (ch.IsCorrect) {
                                res.ans += ch.I1;
                                res.ans += "、";
                            }
                        })
                        s.items[j] = res;
                        this.qesAnswers.push(res);
                        // s.items.splice(j, 1, res);
                        // q.Choices = res.Choices;
                    }
                }
                this.doneAnswer = true;
                console.log("答案", this.qesAnswers);
            },
            async saveHomeWork() {
                var answerSheet = {
                    Items: this.qesAnswers.map(q => {
                        var a = {};
                        a.I1 = q.I1;
                        a.I15 = q.I7;
                        a.Sub = [];
                        if (q.Sub) {
                            a.Sub = q.Sub;
                        }
                        return a;
                    })
                };

                await this.$put("/saveHomeWork", {
                    homeworkID: this.paperJson.HomeworkId,
                    homeworkAnswerId: this.paperJson.HomeworkAnswerId,
                    answerSheet: JSON.stringify(answerSheet),
                });
                this.$confirm('是否提交至学习平台？')
                    .then(_ => {
                        this.$put("/submitHomeWork", {
                            homeworkID: this.paperJson.HomeworkId,
                            homeworkAnswerId: this.paperJson.HomeworkAnswerId,
                            answerSheet: JSON.stringify(answerSheet),
                        }).then(res => {
                            console.log(res);
                            this.$message.success("提交成功,本次分数:" + res.score + '分');
                            this.dialogShow = false;
                        })
                    })
                    .catch(_ => {
                    });
            }
        }
    }
</script>