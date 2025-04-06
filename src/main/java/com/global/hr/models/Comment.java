package com.global.hr.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "Comment") 
public class Comment {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id; 
		@ManyToOne
		@JoinColumn(name = "user_id", nullable = false)
		private User author;
		@ManyToOne
		@JoinColumn(name = "video_id", nullable = false)
		private Video video;
	    private String Content;
	    private Long Likes;
	    private Long disLikes;
	    
	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

		public User getAuthor() {
			return author;
		}

		public void setAuthor(User author) {
			this.author = author;
		}

		public Video getVideo() {
			return video;
		}

		public void setVideo(Video video) {
			this.video = video;
		}

		public String getContent() {
			return Content;
		}

		public void setContent(String content) {
			Content = content;
		}

		public Long getLikes() {
			return Likes;
		}

		public void setLikes(Long likes) {
			Likes = likes;
		}

		public Long getDisLikes() {
			return disLikes;
		}

		public void setDisLikes(Long disLikes) {
			this.disLikes = disLikes;
		}
}
