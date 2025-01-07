package com.rezalaki.booksexplorer.data.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.google.gson.annotations.Expose

@Keep
data class BooksResponse(
    @SerializedName("numFound")
    @Expose val numFound: Int,
    @SerializedName("start")
    @Expose val start: Int? = null,
    @SerializedName("numFoundExact")
    @Expose val numFoundExact: Boolean? = null,
    @SerializedName("docs")
    @Expose val docs: List<Doc>,
    @SerializedName("q")
    @Expose val q: String,
    @SerializedName("offset")
    @Expose val offset: Int? = null
) {

    @Keep
    data class Doc(
        @SerializedName("author_key")
        @Expose val authorKey: List<String>? = null,
        @SerializedName("author_name")
        @Expose val authorName: List<String>? = null,
        @SerializedName("cover_edition_key")
        @Expose val coverEditionKey: String, // OL37695072M
        @SerializedName("cover_i")
        @Expose val coverId: Int, // 12749893
        @SerializedName("ebook_access")
        @Expose val ebookAccess: String? = null, // no_ebook
        @SerializedName("ebook_count_i")
        @Expose val ebookCountI: Int? = null, // 0
        @SerializedName("edition_count")
        @Expose val editionCount: Int? = null, // 1
        @SerializedName("edition_key")
        @Expose val editionKey: List<String>? = null,
        @SerializedName("first_publish_year")
        @Expose val firstPublishYear: Int, // 2022
        @SerializedName("has_fulltext")
        @Expose val hasFulltext: Boolean? = null, // false
        @SerializedName("isbn")
        @Expose val isbn: List<String>? = null,
        @SerializedName("key")
        @Expose val key: String? = null, // /works/OL27645213W
        @SerializedName("language")
        @Expose val language: List<String>? = null,
        @SerializedName("last_modified_i")
        @Expose val lastModifiedI: Int? = null, // 1653001942
        @SerializedName("number_of_pages_median")
        @Expose val numberOfPagesMedian: Int? = null, // 312
        @SerializedName("public_scan_b")
        @Expose val publicScanB: Boolean? = null, // false
        @SerializedName("publish_date")
        @Expose val publishDate: List<String>? = null,
        @SerializedName("publish_year")
        @Expose val publishYear: List<Int>,
        @SerializedName("publisher")
        @Expose val publisher: List<String>? = null,
        @SerializedName("seed")
        @Expose val seed: List<String>? = null,
        @SerializedName("title")
        @Expose val title: String, // Fated to the Alpha
        @SerializedName("title_suggest")
        @Expose val titleSuggest: String? = null, // Fated to the Alpha
        @SerializedName("title_sort")
        @Expose val titleSort: String? = null, // Fated to the Alpha
        @SerializedName("type")
        @Expose val type: String? = null, // work
        @SerializedName("id_amazon")
        @Expose val idAmazon: List<String>? = null,
        @SerializedName("ratings_count_1")
        @Expose val ratingsCount1: Int? = null, // 0
        @SerializedName("ratings_count_2")
        @Expose val ratingsCount2: Int? = null, // 0
        @SerializedName("ratings_count_3")
        @Expose val ratingsCount3: Int? = null, // 0
        @SerializedName("ratings_count_4")
        @Expose val ratingsCount4: Int? = null, // 0
        @SerializedName("ratings_count_5")
        @Expose val ratingsCount5: Int? = null, // 2
        @SerializedName("ratings_average")
        @Expose val ratingsAverage: Double? = null, // 5.0
        @SerializedName("ratings_sortable")
        @Expose val ratingsSortable: Double? = null, // 2.6973765
        @SerializedName("ratings_count")
        @Expose val ratingsCount: Int? = null, // 2
        @SerializedName("readinglog_count")
        @Expose val readinglogCount: Int? = null, // 99
        @SerializedName("want_to_read_count")
        @Expose val wantToReadCount: Int? = null, // 94
        @SerializedName("currently_reading_count")
        @Expose val currentlyReadingCount: Int? = null, // 3
        @SerializedName("already_read_count")
        @Expose val alreadyReadCount: Int? = null, // 2
        @SerializedName("publisher_facet")
        @Expose val publisherFacet: List<String>? = null,
        @SerializedName("_version_")
        @Expose val version: Long? = null, // 1795893204842708992
        @SerializedName("author_facet")
        @Expose val authorFacet: List<String>? = null
    ) {

        fun toBook(): Book = Book(
            id = coverId, // NOTE: response does NOT contain id, so coverId value used instead
            title = title,
            authorName = if (authorName.isNullOrEmpty()) "Not Found Author Name" else authorName.first(),
            firstPublishYear = firstPublishYear,
            coverId = coverId,
            imageUrl = "https://covers.openlibrary.org/b/id/${coverId}-M.jpg"
        )

    }

}