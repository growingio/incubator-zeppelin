package org.apache.zeppelin.utils;

import org.apache.zeppelin.interpreter.InterpreterResultMessage;
import org.apache.zeppelin.notebook.Note;
import org.apache.zeppelin.notebook.Paragraph;
import org.apache.zeppelin.notebook.ParagraphJobListener;
import org.apache.zeppelin.scheduler.Job;
import org.apache.zeppelin.socket.NotebookServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * author CliffLeopard
 * time   2018/10/19:15:30
 * email  gaoguanling@growingio.com
 */
public class GIOParagraphListenerImpl implements ParagraphJobListener {
    private static final Logger gio_logger = LoggerFactory.getLogger("gioparagraph");
    private NotebookServer notebookServer;
    private Note note;

    public GIOParagraphListenerImpl(NotebookServer notebookServer, Note note) {
        this.notebookServer = notebookServer;
        this.note = note;
    }

    @Override
    public void onOutputAppend(Paragraph paragraph, int idx, String output) {

    }

    @Override
    public void onOutputUpdate(Paragraph paragraph, int idx, InterpreterResultMessage msg) {

    }

    @Override
    public void onOutputUpdateAll(Paragraph paragraph, List<InterpreterResultMessage> msgs) {

    }

    @Override
    public void onProgressUpdate(Job job, int progress) {

    }

    @Override
    public void beforeStatusChange(Job job, Job.Status before, Job.Status after) {

    }

    @Override
    public void afterStatusChange(Job job, Job.Status before, Job.Status after) {
        if (job instanceof Paragraph && job.isTerminated()) {
            Paragraph paragraphJob = (Paragraph) job;
            if (paragraphJob.getStatus() == Job.Status.FINISHED) {
                gio_logger.info("GIO Paragraph {}: paragraph_id: {}  info:[note_name:{},user:{},roles:{}]",
                        paragraphJob.getStatus().toString().toUpperCase(), paragraphJob.getId(),
                        paragraphJob.getNote().getName(), paragraphJob.getAuthenticationInfo().getUser(), paragraphJob.getAuthenticationInfo().getRoles().toString());
            } else {
                gio_logger.warn("GIO Paragraph {}: paragraph_id: {}  exception: {}, result: {},info:[note_name:{},user:{},roles:{}]",
                        paragraphJob.getStatus().toString().toUpperCase(), paragraphJob.getId(),
                        paragraphJob.getException(), paragraphJob.getReturn(), paragraphJob.getNote().getName(), paragraphJob.getAuthenticationInfo().getUser(), paragraphJob.getAuthenticationInfo().getRoles().toString());
            }
        }
    }
}
