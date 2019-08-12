package com.climate.fight.recycler;

public class ItemComment {

    private String id, user, commentText;
    private long numLikes, posted;
    private boolean liked;

    public ItemComment(String id, String user, String commentText, long numLikes, long posted, boolean liked){
        this.id = id;
        this.user = user;
        this.commentText = commentText;
        this.numLikes = numLikes;
        this.posted = posted;
        this.liked = liked;
    }


    public String getCommentId() {
        return id;
    }

    public String getCommentText() {
        return commentText;
    }

    public long getNumLikes() {
        return numLikes;
    }

    public boolean isLiked() {
        return liked;
    }

    public long getPostedDate() {
        return posted;
    }

    public String getUser() {
        return user;
    }
}
