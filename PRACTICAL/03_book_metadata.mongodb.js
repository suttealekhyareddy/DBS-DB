use bookflow_db;

db.book_metadata.drop();

db.book_metadata.insertMany([
  {
    title: "The Martian",
    isbn: "9780553418026",
    published_year: 2014,
    format: "Digital E-book",
    technical_specs: { file_size: "2.4 MB", pages: 384 },
    member_id: 1,
    reviews: [
      { member_id: 1, rating: 5, comments: "Amazing space survival story" },
      { member_id: 2, rating: 4, comments: "Very technical and engaging" }
    ]
  },
  {
    title: "Dune",
    isbn: "9780441172719",
    published_year: 1965,
    format: "Digital E-book",
    technical_specs: { file_size: "3.1 MB", pages: 412 },
    member_id: 2,
    reviews: [
      { member_id: 2, rating: 5, comments: "Excellent science fiction classic" },
      { member_id: 3, rating: 5, comments: "Great world building" }
    ]
  },
  {
    title: "Clean Code",
    isbn: "9780132350884",
    published_year: 2008,
    format: "Physical Book",
    technical_specs: { paper_weight: "650 g", dimensions: "23.5 x 18.5 cm" },
    member_id: 3,
    reviews: [
      { member_id: 1, rating: 4, comments: "Useful programming practices" },
      { member_id: 3, rating: 5, comments: "Helpful for software development" }
    ]
  },
  {
    title: "Project Hail Mary",
    isbn: "9780593135204",
    published_year: 2021,
    format: "Digital E-book",
    technical_specs: { file_size: "4.0 MB", pages: 496 },
    member_id: 1,
    reviews: [
      { member_id: 1, rating: 5, comments: "Fantastic space adventure" },
      { member_id: 2, rating: 4, comments: "Fun and scientific" }
    ]
  }
]);

// Comparison operator: rating greater than 4
db.book_metadata.find({ "reviews.rating": { $gt: 4 } });

// $in example
db.book_metadata.find({ published_year: { $in: [2014, 2021] } });

// Regex: format contains Digital
db.book_metadata.find({ format: { $regex: "Digital", $options: "i" } });

// Aggregation pipeline
db.book_metadata.aggregate([
  { $match: { published_year: { $gt: 2020 } } },
  { $unwind: "$reviews" },
  {
    $group: {
      _id: "$title",
      average_rating: { $avg: "$reviews.rating" }
    }
  },
  { $project: { _id: 0, title: "$_id", average_rating: 1 } },
  { $sort: { average_rating: -1 } }
]);

// Text index on nested comments
db.book_metadata.createIndex({ "reviews.comments": "text" });

// Keyword search + explain
db.book_metadata.find(
  { $text: { $search: "space" } }
).explain("executionStats");
