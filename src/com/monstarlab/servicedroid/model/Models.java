package com.monstarlab.servicedroid.model;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Models {

	public static final String AUTHORITY = "com.monstarlab.servicedroid";
	
	private Models() {}
	
	public static final class TimeEntries implements BaseColumns {
		
		private TimeEntries() {}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/time_entries");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.monstarlab.timeentry";
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.monstarlab.timeentry";
		
		public static final String DEFAULT_SORT_ORDER = "date ASC";
		
		public static final long MAX_LENGTH = (24 * 60 * 60) - 1;
		
		/**
		 * 	type: date
		 */
		public static final String DATE = "date";
		
		/**
		 * 	type: integer
		 */
		public static final String LENGTH = "length";
		
		/**
		 * 	type: text
		 */
		public static final String NOTE = "note";
	}
	
	public static final class Calls implements BaseColumns {
		
		private Calls() {}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/calls");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.monstarlab.call";
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.monstarlab.call";
		
		public static final String DEFAULT_SORT_ORDER = "name ASC";
		
		/**
		 * 	type: varchar(128)
		 */
		public static final String NAME = "name";
		
		/**
		 * 	type: varchar(128)
		 */
		public static final String ADDRESS = "address";
		
		/**
		 * 	type: text
		 */
		public static final String NOTES = "notes";
		
		/**
		 * 	type: date
		 */
		public static final String DATE = "date";
		
		/**
		 * 	type: int
		 */
		public static final String TYPE = "type";
		
		/**
		 * 	type: Enum
		 */
		public static final int TYPE_ADDED = 1;
		public static final int TYPE_ANONYMOUS = 2;
		public static final int TYPE_TRANSFERED = 3;
		
		/**
		 * 	type: JOIN
		 */
		public static final String IS_STUDY = "is_study";
		
		/**
		 * 	type: JOIN
		 */
		public static final String LAST_VISITED = "last_visited";
		
	}
	
	public static final class ReturnVisits implements BaseColumns {
		
		private ReturnVisits() {}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/returnvisits");
		
		public static final Uri BIBLE_STUDIES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/returnvisits/biblestudies");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.monstarlab.returnvisit";
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.monstarlab.returnvisit";
		
		public static final String DEFAULT_SORT_ORDER = "date DESC";
		
		/**
		 * 	type: date
		 */
		public static final String DATE = "date";
		
		/**
		 * 	type: varchar(128)
		 */
		public static final String CALL_ID = "call_id";
		
		/**
		 *  type: text
		 */
		public static final String NOTE = "note";
		
		/**
		 *  type: boolean
		 */
		public static final String IS_BIBLE_STUDY = "is_bible_study";
	}
	
	public static final class Placements implements BaseColumns {
		
		private Placements() {}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/placements");
		
		public static final Uri DETAILS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/placements/details");
		public static final Uri MAGAZINES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/placements/magazines");
		public static final Uri BROCHURES_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/placements/brochures");
        public static final Uri TRACTS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/placements/tracts");
		public static final Uri BOOKS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/placements/books");
		public static final Uri VIDEOS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/placements/videos");

		public static final Uri NON_VIDEOS_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/placements/non-videos");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.monstarlab.placement";
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.monstarlab.placement";
		
		public static final String DEFAULT_SORT_ORDER = "date ASC";
		
		/**
		 * 	type: date
		 */
		public static final String DATE = "date";
		
		/**
		 * 	type: date
		 */
		public static final String CALL_ID = "call_id";
		
		/**
		 * 	type: date
		 */
		public static final String LITERATURE_ID = "literature_id";


        /**
         *  type: int
         */
        public static final String QUANTITY = "quantity";
		
	}
	
	public static final class Literature implements BaseColumns {
		
		private Literature() {}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/literature");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.monstarlab.literature";
		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.monstarlab.literature";
		
		public static final String DEFAULT_SORT_ORDER = "_id ASC";
		
		public static final int TYPE_MAGAZINE = 0;
		public static final int TYPE_BROCHURE = 1;
		public static final int TYPE_BOOK = 2;
        public static final int TYPE_TRACT = 3;
		public static final int TYPE_VIDEO = 4;
		
		/**
		 * 	type: varchar
		 */
		public static final String TITLE = "title";
		
		/**
		 * 	type: int
		 */
		public static final String TYPE = "type";
		
		/**
		 * 	type: varchar
		 */
		public static final String PUBLICATION = "publication";
		
		/**
		 *  type: int
		 */
		public static final String WEIGHT = "weight";

		public static final class Periodical {
			public String publication;
			public int month;
			public int year;

			public Periodical() {}

		}

		public static Periodical parseName(String name) {
			int lastSpaceIndex = name.lastIndexOf(" ");
			Periodical periodical = new Periodical();
			periodical.publication = name.substring(0, lastSpaceIndex);
			periodical.month =  Integer.parseInt(name.substring(lastSpaceIndex+1, name.indexOf("/")));
			periodical.year = Integer.parseInt(name.substring(name.indexOf("/")+1));
			return periodical;
		}

	}
	
	
	
}
